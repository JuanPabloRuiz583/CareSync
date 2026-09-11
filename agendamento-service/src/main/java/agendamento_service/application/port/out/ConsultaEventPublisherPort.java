package agendamento_service.application.port.out;

import agendamento_service.domain.Consulta;

public interface ConsultaEventPublisherPort { void publish(Consulta consulta, String eventType); }
