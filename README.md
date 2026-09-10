# CareSync

Backend de agendamento de consultas hospitalares, histórico de pacientes e notificações automáticas. Arquitetura de microsserviços em Spring Boot combinando **GraphQL** (porta de entrada única, client-facing), **gRPC** (comunicação interna síncrona) e **RabbitMQ** (comunicação assíncrona).

## Arquitetura

```
                       CLIENTE (Postman / navegador)
                                 │
                                 │ HTTP + GraphQL
                                 ▼
                       ┌────────────────────┐
                       │     graphql-api     │  :8081
                       │   GraphQL Server    │
                       │    gRPC Client      │
                       └──────────┬──────────┘
                        ┌─────────┴─────────┐
                        │ gRPC              │ gRPC
                        ▼                   ▼
            ┌─────────────────────┐ ┌──────────────────────┐
            │   patient-service    │ │  agendamento-service  │
            │   gRPC :9090          │ │   gRPC :9091            │
            │   HTTP :8080 (H2 UI)  │ │   HTTP :8082 (H2 UI)    │
            │   H2 (patientdb)      │ │   H2 (agendamentodb)    │
            └─────────────────────┘ └───────────┬──────────┘
                                                  │ publica evento
                                                  │ (consulta.created / consulta.updated)
                                                  ▼
                                       ┌────────────────────┐
                                       │      RabbitMQ        │  :5672 (AMQP) / :15672 (painel)
                                       │  exchange: consulta.exchange
                                       │  fila: notificacao.queue
                                       └──────────┬──────────┘
                                                  │ consome
                                                  ▼
                                       ┌────────────────────┐
                                       │ notificacao-service   │  sem porta exposta —
                                       │  (@RabbitListener)     │  só escuta a fila
                                       └────────────────────┘
```

- **`graphql-api` é a única porta de entrada.** O cliente nunca fala direto com `patient-service` ou `agendamento-service`.
- **`patient-service` e `agendamento-service` são servidores gRPC puros**, cada um dono do próprio banco H2, e **não se conhecem entre si**.
- **`agendamento-service` nunca fala diretamente com `notificacao-service`** — só publica um evento no RabbitMQ; quem consome nem quando isso acontece é problema do produtor (comunicação assíncrona de verdade).
- **`notificacao-service` não expõe HTTP nem gRPC** — é um serviço "de fundo", só reage a eventos da fila.
- Cada serviço é um **projeto Maven independente** (`pom.xml` e Maven Wrapper próprios), sem módulo pai compartilhado — versionamento e deploy independentes.

## Os 4 serviços

### `graphql-api` — porta de entrada (HTTP 8081)
- Expõe a API GraphQL (`POST /graphql`, GraphiQL em `/graphiql`).
- Não tem banco de dados nem lógica de persistência própria.
- É **gRPC client** dos outros dois serviços: traduz queries/mutations GraphQL em chamadas gRPC internas e devolve o resultado combinado.
- Camadas internas: `Controller` (`@QueryMapping`/`@MutationMapping`) → `Service` (orquestração, validações entre serviços) → `Mapper` (proto ↔ DTO GraphQL).

### `patient-service` — dono de `Patient` (HTTP 8080 / gRPC 9090)
- Persiste `Patient` (id, nome, email) em H2 (`patientdb`) via JPA/Hibernate.
- Expõe uma única operação gRPC: `FindById`.

### `agendamento-service` — dono de `Consulta` (HTTP 8082 / gRPC 9091)
- Persiste `Consulta` (paciente, médico, data/hora, status, motivo) em H2 (`agendamentodb`) via JPA/Hibernate.
- Expõe via gRPC: `Create`, `Update`, `ListByPatient` (histórico completo), `ListUpcomingByPatient` (só futuras).
- **Não conhece o `patient-service`** — a validação de "esse paciente existe?" antes de criar uma consulta é feita no `graphql-api`, que é o único que fala com os dois.
- Ao criar ou editar uma consulta, publica um evento `ConsultaEvent` no RabbitMQ (routing key `consulta.created` ou `consulta.updated`).

### `notificacao-service` — consumidor de eventos (sem porta exposta)
- Não tem banco de dados, não expõe nenhuma API (nem HTTP, nem gRPC) — só um `@RabbitListener` ouvindo a `notificacao.queue`.
- Ao receber um evento, "envia o lembrete" ao paciente (simulado via log estruturado, já que não há integração real de SMS/e-mail).

## Comunicação Assíncrona (RabbitMQ)

- **Exchange**: `consulta.exchange` (tipo `topic`)
- **Fila**: `notificacao.queue`, ligada ao exchange com o binding `consulta.*`
- **Routing keys**: `consulta.created` (ao criar) e `consulta.updated` (ao editar) — ambas caem na mesma fila graças ao padrão `*` do binding
- Mensagens serializadas em **JSON** (`Jackson2JsonMessageConverter`)
- Painel de administração: `http://localhost:15672` (usuário/senha: `guest`/`guest`)

## Segurança

Autenticação via **JWT**: `POST /auth/login` (usuário/senha) devolve um token, que deve ser enviado em `Authorization: Bearer <token>` em toda chamada ao `/graphql`. Usuários persistidos em banco (H2, `usersdb`), senha em hash BCrypt. Autorização por role, aplicada por operação GraphQL:

| Operação | Médico | Enfermeiro | Paciente |
|---|---|---|---|
| `patient` | ✅ (qualquer paciente) | ✅ (qualquer paciente) | ✅ só o próprio |
| `consultasDoPaciente` / `consultasFuturasDoPaciente` | ✅ (qualquer paciente) | ✅ (qualquer paciente) | ✅ só as próprias |
| `agendarConsulta` | ❌ | ✅ | ❌ |
| `editarConsulta` | ✅ | ❌ | ❌ |

Usuários de teste (populados no banco na inicialização, senha `senha123` para todos):

| Usuário | Role | Paciente vinculado |
|---|---|---|
| `admin1` | ADMIN | — |
| `medico1` | MEDICO | — |
| `enfermeiro1` | ENFERMEIRO | — |
| `paciente1` | PACIENTE | id 1 |
| `paciente2` | PACIENTE | id 2 |

### Gestão de usuários (só `ADMIN`)

| Endpoint | Descrição |
|---|---|
| `POST /users` | cria usuário — body `{username, password, role, patientId}` |
| `GET /users` | lista todos os usuários |
| `DELETE /users/{id}` | remove um usuário |

## Documentação da API REST (Swagger)

`/auth/login` e `/users/*` (endpoints REST) documentados automaticamente via Swagger UI: `http://localhost:8081/swagger-ui/index.html`. O `/graphql` tem sua própria documentação, via GraphiQL/introspection — Swagger não se aplica a operações GraphQL.

## Contratos gRPC (`.proto`)

Cada `.proto` (`patient.proto`, `agendamento.proto`) é **duplicado** entre o serviço dono e o `graphql-api` — cliente e servidor geram suas próprias classes a partir do mesmo contrato, já que não existe módulo Maven compartilhado entre os projetos.

## Stack técnica

- Java 17, Spring Boot 4.1
- Spring for GraphQL (`spring-boot-starter-graphql`)
- gRPC nativo do Spring Boot 4.1 (`spring-boot-starter-grpc-server` / `spring-boot-starter-grpc-client`) + Protocol Buffers (`protobuf-maven-plugin`)
- Spring Data JPA + H2 (um banco em memória por serviço)
- RabbitMQ (`spring-boot-starter-amqp`) — comunicação assíncrona entre `agendamento-service` e `notificacao-service`
- Spring Security + JWT (`io.jsonwebtoken:jjwt`)

## Como rodar

**Mais simples: Docker Compose**, sobe RabbitMQ + os 4 serviços juntos, na ordem certa:

```bash
docker compose up --build
```

Ou local — precisa do RabbitMQ rodando antes (`docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:4-management`), depois:

```bash
cd patient-service && ./mvnw spring-boot:run
cd agendamento-service && ./mvnw spring-boot:run
cd notificacao-service && ./mvnw spring-boot:run
cd graphql-api && ./mvnw spring-boot:run
```

GraphQL: `http://localhost:8081/graphql` — GraphiQL: `http://localhost:8081/graphiql`

## Status dos requisitos da documentação do desafio

| Requisito | Status |
|---|---|
| GraphQL (consultas flexíveis, histórico/futuras) | ✅ |
| Serviço de Agendamento (criar/editar consulta) | ✅ |
| Separação em serviços — Agendamento | ✅ |
| Separação em serviços — Notificações | ✅ |
| Segurança (Spring Security + níveis de acesso) | ✅ |
| Comunicação assíncrona (RabbitMQ/Kafka) | ✅ RabbitMQ |
| Collection Postman/Insomnia | ✅ [`CareSync.postman_collection.json`](CareSync.postman_collection.json) |
