# CareSync

[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-API-E10098?logo=graphql&logoColor=white)](https://graphql.org/)
[![gRPC](https://img.shields.io/badge/gRPC-Protobuf-244C5A?logo=google&logoColor=white)](https://grpc.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-4-FF6600?logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-%E2%89%A595%25-brightgreen)](https://www.jacoco.org/jacoco/)

Backend hospitalar modular para autenticação por perfil, consulta de pacientes e históricos, agendamento de consultas e notificações assíncronas. A entrada pública combina REST para autenticação/administração e GraphQL para pacientes e consultas; a comunicação interna usa gRPC e RabbitMQ.

## Arquitetura

```mermaid
flowchart LR
    C[Cliente / Postman] -->|REST + GraphQL + JWT| G[graphql-api :8081]
    G -->|gRPC :9090| P[patient-service]
    G -->|gRPC :9091| A[agendamento-service]
    P --> PDB[(PostgreSQL patientdb)]
    A --> ADB[(PostgreSQL agendamentodb)]
    G --> UDB[(PostgreSQL authdb)]
    A -->|consulta.created / consulta.updated| R[(RabbitMQ)]
    R --> N[notificacao-service]
    N -->|porta NotificationSender| L[Log estruturado]
```

Cada serviço segue arquitetura hexagonal:

```mermaid
flowchart LR
    IN[Adaptadores de entrada<br/>REST / GraphQL / gRPC / RabbitMQ]
    UC[Aplicação<br/>casos de uso e portas]
    D[Domínio<br/>modelos e regras]
    OUT[Adaptadores de saída<br/>JPA / gRPC / RabbitMQ / JWT / Log]
    IN --> UC
    UC --> D
    UC -->|interfaces de saída| OUT
    OUT -. implementa .-> UC
```

| Serviço | Responsabilidade | Entrada | Saídas | Persistência |
|---|---|---|---|---|
| `graphql-api` | Gateway, JWT, autorização e usuários | REST/GraphQL `:8081` | gRPC, JWT, BCrypt | PostgreSQL `authdb` |
| `patient-service` | Consulta de pacientes | gRPC `:9090` | JPA | PostgreSQL `patientdb` |
| `agendamento-service` | Criar, editar e listar consultas | gRPC `:9091` | JPA e RabbitMQ | PostgreSQL `agendamentodb` |
| `notificacao-service` | Consumir eventos e enviar lembrete simulado | RabbitMQ | Log estruturado | Stateless |

### Fluxo de agendamento

```mermaid
sequenceDiagram
    actor E as Enfermeiro
    participant G as graphql-api
    participant P as patient-service
    participant A as agendamento-service
    participant R as RabbitMQ
    participant N as notificacao-service
    E->>G: mutation agendarConsulta + JWT
    G->>P: FindById(patientId)
    P-->>G: Patient
    G->>A: Create
    A->>A: validar e persistir
    A->>R: consulta.created
    A-->>G: Consulta
    G-->>E: resultado GraphQL
    R->>N: ConsultaEvent
    N->>N: validar e registrar lembrete no log
```

## Segurança e contratos

`POST /auth/login` recebe `{"username":"...","password":"..."}` e devolve `{"token":"..."}`. Use `Authorization: Bearer <token>` nas demais chamadas.

| Operação | ADMIN | MÉDICO | ENFERMEIRO | PACIENTE |
|---|:---:|:---:|:---:|:---:|
| Gerenciar `/users` | ✅ | ❌ | ❌ | ❌ |
| Consultar paciente/histórico | ❌ | ✅ qualquer | ✅ qualquer | ✅ próprio |
| `agendarConsulta` | ❌ | ❌ | ✅ | ❌ |
| `editarConsulta` | ❌ | ✅ | ❌ | ❌ |

GraphQL (`POST /graphql`, GraphiQL em `/graphiql`):

- `patient(id)`
- `consultasDoPaciente(patientId)`
- `consultasFuturasDoPaciente(patientId)`
- `agendarConsulta(input)`
- `editarConsulta(input)`

REST administrativo (`ADMIN`): `POST /users` retorna `201`, `GET /users` retorna `200` e `DELETE /users/{id}` retorna `204`. Swagger UI: `http://localhost:8081/swagger-ui/index.html`.

Eventos AMQP usam o exchange `consulta.exchange`, routing keys `consulta.created` e `consulta.updated`, fila `notificacao.queue` e dead-letter queue `notificacao.dlq`. O adaptador atual simula o envio do lembrete por log estruturado.

## Executar

Pré-requisitos: Docker com Compose. A opção local também exige Java 17+ e RabbitMQ/PostgreSQL nas portas indicadas.

```bash
docker compose up --build
```

Serviços expostos:

- GraphQL/API: `http://localhost:8081`
- gRPC pacientes: `localhost:9090`
- gRPC agendamentos: `localhost:9091`
- RabbitMQ Management: `http://localhost:15672` (`guest` / `guest`)
- PostgreSQL: pacientes `5432`, agendamentos `5433`, autenticação `5434`

Os schemas são versionados com Flyway. A massa é idempotente e inclui:

| Usuário | Senha | Perfil/vínculo |
|---|---|---|
| `admin1` | `senha123` | ADMIN |
| `medico1` | `senha123` | MEDICO |
| `enfermeiro1` | `senha123` | ENFERMEIRO |
| `paciente1` | `senha123` | PACIENTE / paciente 1 |
| `paciente2` | `senha123` | PACIENTE / paciente 2 |

Também existem dois pacientes, uma consulta passada e duas futuras. Essas credenciais são exclusivamente acadêmicas; configure `JWT_SECRET` e senhas próprias fora do ambiente local.

Para execução sem Docker, inicie os três PostgreSQL e o RabbitMQ, configure as variáveis descritas em `application.properties` e execute cada serviço em um terminal:

```bash
./mvnw spring-boot:run
```

## Testar

Cada módulo possui gate JaCoCo mínimo de 95% para linhas e branches. Apenas classes geradas pelo Protobuf são excluídas.

```bash
cd patient-service && ./mvnw clean verify
cd agendamento-service && ./mvnw clean verify
cd notificacao-service && ./mvnw clean verify
cd graphql-api && ./mvnw clean verify
```

Relatórios: `<serviço>/target/site/jacoco/index.html`.

Teste funcional completo:

```bash
npx newman run CareSync.postman_collection.json
```

A collection autentica todos os perfis, cria e remove usuário, valida isolamento de paciente, consulta histórico e futuras, agenda e edita uma consulta usando IDs/datas dinâmicos e cobre autenticação, autorização e entradas inválidas.

Para reiniciar também os dados locais:

```bash
docker compose down -v
docker compose up --build
```

## Decisões e limites

- Cada serviço persistente é dono de seu banco; não há joins entre microsserviços.
- O `notificacao-service` permanece stateless e a entrega externa é uma porta substituível; e-mail/SMS não fazem parte desta versão.
- Datas usam ISO-8601 local, por exemplo `2026-10-01T14:30:00`; novas consultas precisam estar no futuro.
- Status aceitos: `SCHEDULED`, `COMPLETED` e `CANCELLED`.
- Falhas permanentes no consumo RabbitMQ seguem para `notificacao.dlq` após três tentativas.

A documentação técnica completa está em `output/pdf/caresync-documentacao.pdf`.
