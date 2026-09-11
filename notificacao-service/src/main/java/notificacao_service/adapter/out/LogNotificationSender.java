package notificacao_service.adapter.out;

import notificacao_service.application.port.out.NotificationSenderPort;
import notificacao_service.domain.Notification;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Component
public class LogNotificationSender implements NotificationSenderPort {
    private static final Logger log = LoggerFactory.getLogger(LogNotificationSender.class);
    @Override public void send(Notification n) {
        log.info("notification_sent consultaId={} patientId={} title={} doctor={} dateTime={} reason={}", n.consultaId(), n.patientId(), n.title(), n.doctorName(), n.dateTime(), n.reason());
    }
}
