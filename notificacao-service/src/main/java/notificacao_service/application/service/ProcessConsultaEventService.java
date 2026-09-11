package notificacao_service.application.service;

import notificacao_service.application.port.in.ProcessConsultaEventUseCase;
import notificacao_service.application.port.out.NotificationSenderPort;
import notificacao_service.domain.Notification;
import notificacao_service.event.ConsultaEvent;

public class ProcessConsultaEventService implements ProcessConsultaEventUseCase {
    private final NotificationSenderPort sender;
    public ProcessConsultaEventService(NotificationSenderPort sender) { this.sender=sender; }
    @Override public void process(ConsultaEvent event) {
        if (event == null) throw new IllegalArgumentException("Consulta event is required");
        String title = switch (event.eventType()) {
            case "CREATED" -> "Nova consulta agendada";
            case "UPDATED" -> "Consulta atualizada";
            default -> throw new IllegalArgumentException("Unsupported event type: " + event.eventType());
        };
        sender.send(new Notification(event.consultaId(), event.patientId(), title, event.doctorName(), event.dateTime(), event.reason()));
    }
}
