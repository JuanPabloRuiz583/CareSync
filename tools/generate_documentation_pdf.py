from pathlib import Path
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle, KeepTogether
from reportlab.graphics.shapes import Drawing, Rect, String, Line, Polygon

ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "output" / "pdf" / "caresync-documentacao.pdf"
OUTPUT.parent.mkdir(parents=True, exist_ok=True)

NAVY = colors.HexColor("#17324D")
BLUE = colors.HexColor("#2D6CDF")
CYAN = colors.HexColor("#DCEBFF")
GREEN = colors.HexColor("#1F8A70")
ORANGE = colors.HexColor("#E7792B")
LIGHT = colors.HexColor("#F4F7FA")
GRAY = colors.HexColor("#5F6B76")
RED = colors.HexColor("#B42318")

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="CoverTitle", parent=styles["Title"], fontName="Helvetica-Bold", fontSize=30, leading=34, textColor=NAVY, alignment=TA_CENTER, spaceAfter=10*mm))
styles.add(ParagraphStyle(name="CoverSub", parent=styles["Normal"], fontSize=13, leading=19, textColor=GRAY, alignment=TA_CENTER))
styles.add(ParagraphStyle(name="H1x", parent=styles["Heading1"], fontName="Helvetica-Bold", fontSize=21, leading=25, textColor=NAVY, spaceAfter=7*mm))
styles.add(ParagraphStyle(name="H2x", parent=styles["Heading2"], fontName="Helvetica-Bold", fontSize=13, leading=17, textColor=BLUE, spaceBefore=4*mm, spaceAfter=2*mm))
styles.add(ParagraphStyle(name="Bodyx", parent=styles["BodyText"], fontName="Helvetica", fontSize=9.5, leading=14, textColor=colors.HexColor("#25313C"), spaceAfter=2.5*mm))
styles.add(ParagraphStyle(name="Smallx", parent=styles["BodyText"], fontSize=8, leading=11, textColor=GRAY))
styles.add(ParagraphStyle(name="HeaderCell", parent=styles["Smallx"], fontName="Helvetica-Bold", textColor=colors.white))
styles.add(ParagraphStyle(name="Codex", parent=styles["Code"], fontName="Courier", fontSize=7.4, leading=10, backColor=LIGHT, borderColor=colors.HexColor("#D5DEE7"), borderWidth=.5, borderPadding=6, spaceAfter=3*mm))
styles.add(ParagraphStyle(name="Callout", parent=styles["BodyText"], fontSize=9, leading=13, textColor=NAVY, backColor=CYAN, borderColor=BLUE, borderWidth=.7, borderPadding=8, spaceAfter=4*mm))

def P(text, style="Bodyx"):
    return Paragraph(text, styles[style])

def bullets(items):
    return [P("- " + item) for item in items]

def table(data, widths=None, header=True):
    rows = [
        [P(str(c), "HeaderCell" if header and row_index == 0 else "Smallx") for c in row]
        for row_index, row in enumerate(data)
    ]
    t = Table(rows, colWidths=widths, repeatRows=1 if header else 0, hAlign="LEFT")
    commands = [
        ("VALIGN", (0,0), (-1,-1), "TOP"), ("GRID", (0,0), (-1,-1), .4, colors.HexColor("#C7D1DB")),
        ("LEFTPADDING", (0,0), (-1,-1), 5), ("RIGHTPADDING", (0,0), (-1,-1), 5),
        ("TOPPADDING", (0,0), (-1,-1), 5), ("BOTTOMPADDING", (0,0), (-1,-1), 5),
        ("ROWBACKGROUNDS", (0,1 if header else 0), (-1,-1), [colors.white, LIGHT]),
    ]
    if header:
        commands += [("BACKGROUND", (0,0), (-1,0), NAVY), ("TEXTCOLOR", (0,0), (-1,0), colors.white)]
    t.setStyle(TableStyle(commands))
    return t

def arrow(d, x1, y1, x2, y2, color=GRAY):
    d.add(Line(x1,y1,x2,y2,strokeColor=color,strokeWidth=1.5))
    d.add(Polygon([x2,y2,x2-5,y2+3,x2-5,y2-3],fillColor=color,strokeColor=color))

def box(d, x, y, w, h, title, subtitle, fill=CYAN):
    d.add(Rect(x,y,w,h,rx=5,ry=5,fillColor=fill,strokeColor=BLUE,strokeWidth=1))
    d.add(String(x+w/2,y+h-15,title,textAnchor="middle",fontName="Helvetica-Bold",fontSize=9,fillColor=NAVY))
    d.add(String(x+w/2,y+10,subtitle,textAnchor="middle",fontName="Helvetica",fontSize=6.8,fillColor=GRAY))

def general_diagram():
    d=Drawing(480,225)
    box(d,5,92,90,46,"Cliente","REST + GraphQL + JWT",colors.HexColor("#FFF3D6"))
    box(d,125,92,105,46,"graphql-api","gateway + auth",CYAN)
    box(d,275,158,100,42,"patient-service","gRPC + JPA",colors.HexColor("#E5F5EE"))
    box(d,275,92,100,42,"agendamento","gRPC + eventos",colors.HexColor("#E5F5EE"))
    box(d,275,25,100,42,"notificacao","Rabbit listener",colors.HexColor("#FFE9E1"))
    box(d,405,158,70,42,"patientdb","PostgreSQL",LIGHT)
    box(d,405,92,70,42,"agenda db","PostgreSQL",LIGHT)
    box(d,405,25,70,42,"RabbitMQ","queue + DLQ",LIGHT)
    box(d,125,25,105,42,"authdb","PostgreSQL",LIGHT)
    arrow(d,95,115,125,115); arrow(d,230,118,275,178); arrow(d,230,108,275,112); arrow(d,375,179,405,179); arrow(d,375,113,405,113); arrow(d,375,103,405,46); arrow(d,405,46,375,46); arrow(d,230,92,230,46)
    return d

def hex_diagram(inbound, outbound):
    d=Drawing(480,150)
    box(d,8,52,95,48,"Entrada",inbound,colors.HexColor("#FFF3D6"))
    box(d,135,38,100,76,"Aplicacao","casos de uso + portas",CYAN)
    box(d,270,52,82,48,"Dominio","regras puras",colors.HexColor("#E5F5EE"))
    box(d,385,52,88,48,"Saida",outbound,colors.HexColor("#FFE9E1"))
    arrow(d,103,76,135,76); arrow(d,235,76,270,76); arrow(d,352,76,385,76)
    return d

def header_footer(canvas, doc):
    canvas.saveState()
    canvas.setStrokeColor(colors.HexColor("#D5DEE7")); canvas.line(20*mm,18*mm,190*mm,18*mm)
    canvas.setFont("Helvetica",7.5); canvas.setFillColor(GRAY)
    canvas.drawString(20*mm,12*mm,"CareSync - Documentacao tecnica")
    canvas.drawRightString(190*mm,12*mm,f"Pagina {doc.page}")
    canvas.restoreState()

story=[]
story += [Spacer(1,30*mm), P("CARESYNC","CoverTitle"), P("Arquitetura, casos de uso, execução e testes","CoverSub"), Spacer(1,18*mm), general_diagram(), Spacer(1,12*mm), P("Tech Challenge - Fase 3", "CoverSub"), P("Versão técnica consolidada - Setembro de 2026", "CoverSub"), PageBreak()]

story += [P("1. Visão geral", "H1x"), P("CareSync é um backend hospitalar composto por quatro serviços independentes. O sistema protege operações por perfil, oferece consultas flexíveis via GraphQL, usa gRPC na comunicação síncrona interna e RabbitMQ para notificação assíncrona."), P("Objetivos atendidos", "H2x")]
story += bullets(["Autenticação JWT implementada com Spring Security e senhas BCrypt.","Médicos visualizam históricos e editam consultas; enfermeiros visualizam e agendam; pacientes acessam apenas seus próprios dados.","Histórico completo e consultas futuras disponíveis via GraphQL.","Eventos publicados em criação e edição, com retry e dead-letter queue.","PostgreSQL isolado por serviço persistente e migrações Flyway.","Gate JaCoCo de 95% para linhas e branches em cada módulo."])
story += [P("Matriz de componentes", "H2x"), table([
    ["Componente","Responsabilidade","Tecnologia","Dados"],
    ["graphql-api","Gateway, segurança e usuários","REST, GraphQL, JWT, gRPC","authdb"],
    ["patient-service","Localizar pacientes","gRPC, JPA","patientdb"],
    ["agendamento-service","Criar, editar e listar consultas","gRPC, JPA, AMQP","agendamentodb"],
    ["notificacao-service","Consumir eventos e enviar lembrete","RabbitMQ, log estruturado","Stateless"],
], [32*mm,58*mm,45*mm,30*mm]), P("Decisão importante", "H2x"), P("O envio externo foi modelado como porta. Nesta versão, o adaptador grava um log estruturado; SMTP, SMS ou push podem ser adicionados sem alterar o caso de uso.","Callout"), PageBreak()]

story += [P("2. Arquitetura geral", "H1x"), general_diagram(), P("Princípios", "H2x")]
story += bullets(["Database per service: nenhum serviço consulta diretamente o banco de outro.","Dependências apontam para dentro: adaptadores dependem de portas; domínio não conhece frameworks.","graphql-api é a única entrada pública funcional.","Contratos gRPC são definidos por Protobuf e gerados separadamente no cliente e servidor.","Mensageria desacopla agendamento e notificação; falhas permanentes seguem para notificacao.dlq."])
story += [P("Fluxo síncrono e assíncrono", "H2x"), table([
    ["Etapa","Origem","Destino","Resultado"],
    ["1","Cliente","graphql-api","JWT autenticado e role autorizada"],
    ["2","graphql-api","patient-service","Paciente validado por gRPC"],
    ["3","graphql-api","agendamento-service","Consulta persistida por gRPC"],
    ["4","agendamento-service","RabbitMQ","ConsultaEvent publicado"],
    ["5","RabbitMQ","notificacao-service","Lembrete processado ou enviado à DLQ"],
], [12*mm,38*mm,42*mm,78*mm]), PageBreak()]

services = [
    ("3. graphql-api", "REST / GraphQL", "gRPC / JPA / JWT", ["Autenticar usuário e emitir JWT.","Criar, listar e remover usuários administrativos.","Orquestrar paciente e consultas por gRPC.","Aplicar autorização por perfil e propriedade do paciente."], ["POST /auth/login","POST, GET e DELETE /users","POST /graphql","GET /graphiql e /swagger-ui/index.html"], "PostgreSQL authdb. Flyway cria app_users; a carga idempotente gera cinco contas acadêmicas com BCrypt."),
    ("4. patient-service", "gRPC FindById", "JPA / PostgreSQL", ["Validar identificador positivo.","Localizar o paciente por sua porta de repositório.","Retornar NOT_FOUND sem expor detalhes internos.","Mapear domínio para o contrato Protobuf."], ["PatientService.FindById(PatientRequest)","PatientResponse: id, name, email"], "PostgreSQL patientdb. Flyway cria patients e carrega Maria Silva e Joao Souza com IDs determinísticos."),
    ("5. agendamento-service", "gRPC Create / Update / List", "JPA / RabbitMQ", ["Agendar somente no futuro e com campos obrigatórios.","Editar consulta existente e validar status.","Listar histórico ordenado e filtrar somente futuras.","Publicar CREATED ou UPDATED após persistência."], ["Create","Update","ListByPatient","ListUpcomingByPatient"], "PostgreSQL agendamentodb. Status: SCHEDULED, COMPLETED e CANCELLED. Índice por patient_id e date_time."),
    ("6. notificacao-service", "RabbitMQ listener", "NotificationSender / log", ["Consumir ConsultaEvent.","Aceitar CREATED e UPDATED.","Construir uma notificação de domínio validada.","Repetir até três vezes e rotear falhas permanentes à DLQ."], ["Exchange consulta.exchange","Fila notificacao.queue","DLQ notificacao.dlq","Binding consulta.*"], "Serviço stateless. Não possui banco por decisão arquitetural; duplicidade deve ser tratada pelo futuro adaptador externo quando necessário."),
]
for title, inbound, outbound, cases, contracts, data in services:
    story += [P(title,"H1x"), hex_diagram(inbound,outbound), P("Casos de uso","H2x")]
    story += bullets(cases)
    story += [P("Contratos e interfaces","H2x"), table([["Interface","Descrição"]]+[[c,"Contrato ativo"] for c in contracts],[70*mm,95*mm]), P("Dados e operação","H2x"), P(data), PageBreak()]

story += [P("7. Segurança e API", "H1x"), P("O login público autentica username e senha. O token assinado contém o subject, emissão e expiração de uma hora. Requisições sem token, com token inválido ou expirado recebem HTTP 401."), table([
    ["Operação","ADMIN","MÉDICO","ENFERMEIRO","PACIENTE"],
    ["Gerenciar usuários","Sim","Não","Não","Não"],
    ["Consultar paciente/histórico","Não","Qualquer","Qualquer","Próprio"],
    ["Agendar consulta","Não","Não","Sim","Não"],
    ["Editar consulta","Não","Sim","Não","Não"],
], [55*mm,25*mm,27*mm,30*mm,28*mm]), P("Exemplos", "H2x"), P('POST /auth/login<br/><font name="Courier">{"username":"medico1","password":"senha123"}</font>'), P('GraphQL - Authorization: Bearer &lt;token&gt;<br/><font name="Courier">query { consultasFuturasDoPaciente(patientId: 1) { id dateTime status } }</font>'), P("Erros", "H2x")]
story += bullets(["BAD_REQUEST: entrada ou regra de negócio inválida.","FORBIDDEN: role sem permissão ou paciente tentando acessar outro cadastro.","NOT_FOUND: consulta ou paciente inexistente.","INTERNAL_ERROR: dependência indisponível sem vazamento de stack trace."])
story += [PageBreak(), P("8. Bancos e mensageria", "H1x"), P("O Docker Compose sobe três instâncias PostgreSQL independentes. Cada aplicação valida o schema após o Flyway executar; Hibernate não cria nem altera tabelas."), table([
    ["Banco","Porta local","Tabela","Massa"],
    ["patientdb","5432","patients","2 pacientes"],
    ["agendamentodb","5433","consultas","1 passada e 2 futuras"],
    ["authdb","5434","app_users","5 perfis"],
], [42*mm,30*mm,42*mm,48*mm]), P("Evento ConsultaEvent", "H2x"), P("consultaId, patientId, doctorName, dateTime, reason e eventType. O produtor usa routing key consulta.created ou consulta.updated. O consumidor rejeita tipos desconhecidos, ativa retry e encaminha a mensagem à notificacao.dlq após esgotar três tentativas."), P("Consistência", "H2x"), P("A consulta é salva antes da publicação. Se o broker estiver indisponível, a chamada falha de forma observável; para garantias de entrega exatamente após commit em um ambiente produtivo, recomenda-se evoluir para transactional outbox."), PageBreak()]

story += [P("9. Como executar", "H1x"), P("Pré-requisito: Docker Desktop ou Docker Engine com Compose em execução."), P("docker compose up --build", "Codex"), P("Endereços", "H2x"), table([
    ["Recurso","Endereço"],["GraphQL","http://localhost:8081/graphql"],["GraphiQL","http://localhost:8081/graphiql"],["Swagger","http://localhost:8081/swagger-ui/index.html"],["RabbitMQ","http://localhost:15672"],["gRPC pacientes","localhost:9090"],["gRPC consultas","localhost:9091"],
], [55*mm,105*mm]), P("Configuração", "H2x")]
story += bullets(["PATIENT_DB_URL, PATIENT_DB_USER, PATIENT_DB_PASSWORD.","AGENDAMENTO_DB_URL, AGENDAMENTO_DB_USER, AGENDAMENTO_DB_PASSWORD.","AUTH_DB_URL, AUTH_DB_USER, AUTH_DB_PASSWORD.","RABBITMQ_HOST, PATIENT_SERVICE_HOST, AGENDAMENTO_SERVICE_HOST e JWT_SECRET."])
story += [P("Reinício limpo", "H2x"), P("docker compose down -v<br/>docker compose up --build", "Codex"), PageBreak()]

story += [P("10. Como testar", "H1x"), P("Cada módulo executa testes e o gate JaCoCo no lifecycle verify. O build falha se linhas ou branches ficarem abaixo de 95%. Somente fontes Java geradas pelo Protobuf são excluídas."), P("cd patient-service &amp;&amp; ./mvnw clean verify<br/>cd agendamento-service &amp;&amp; ./mvnw clean verify<br/>cd notificacao-service &amp;&amp; ./mvnw clean verify<br/>cd graphql-api &amp;&amp; ./mvnw clean verify", "Codex"), P("Cobertura validada", "H2x"), table([
    ["Módulo","Linhas","Branches","Gate"],
    ["patient-service","97.22%","100%","Aprovado"],
    ["agendamento-service","98.95%","100%","Aprovado"],
    ["notificacao-service","97.14%","100%","Aprovado"],
    ["graphql-api","99.55%","100%","Aprovado"],
], [60*mm,32*mm,32*mm,35*mm]), P("Aceitação funcional", "H2x"), P("npx newman run CareSync.postman_collection.json", "Codex")]
story += bullets(["Login válido para cinco perfis e login inválido.","Criação, listagem, autorização e limpeza de usuário.","Acesso próprio do paciente e bloqueio de acesso cruzado.","Histórico completo e filtro de futuras.","Agendamento por enfermeiro e edição por médico com ID dinâmico.","Negativas por role, data inválida, paciente inexistente, ausência e corrupção de token."])
story += [PageBreak(), P("11. Operação e troubleshooting", "H1x"), table([
    ["Sintoma","Verificação","Ação"],
    ["Docker não conecta","Docker Engine/pipe indisponível","Iniciar Docker Desktop e repetir compose up"],
    ["Migration falha","Logs Flyway e credenciais","Revisar URL/usuário; em dev, recriar volumes"],
    ["UNAVAILABLE no GraphQL","Serviços gRPC e portas 9090/9091","Aguardar inicialização e conferir hosts"],
    ["Evento na DLQ","RabbitMQ Management e logs","Corrigir payload/adaptador e reprocesar conscientemente"],
    ["401","Header e expiração JWT","Autenticar novamente e usar Bearer token"],
], [42*mm,55*mm,68*mm]), P("Checklist de entrega", "H2x")]
story += bullets(["Quatro módulos compilam e executam testes.","Gates JaCoCo aprovados para linhas e branches.","Collection Postman é JSON válido e executável em sequência.","Docker Compose é sintaticamente válido e declara healthchecks.","Schemas e massas são determinísticos e idempotentes.","README contém diagramas Mermaid, badges, contratos e instruções.","PDF foi extraído, renderizado e inspecionado página a página."])
story += [Spacer(1,8*mm), P("Fim da documentação", "Callout")]

doc=SimpleDocTemplate(str(OUTPUT),pagesize=A4,rightMargin=20*mm,leftMargin=20*mm,topMargin=20*mm,bottomMargin=24*mm,title="CareSync - Documentação Técnica",author="CareSync")
doc.build(story,onFirstPage=header_footer,onLaterPages=header_footer)
print(OUTPUT)
