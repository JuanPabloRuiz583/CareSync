package notificacao_service.application.port.in;

import notificacao_service.event.ConsultaEvent;

public interface ProcessConsultaEventUseCase { void process(ConsultaEvent event); }
