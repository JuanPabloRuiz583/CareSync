package notificacao_service.adapter.out;

import notificacao_service.domain.Notification;
import org.junit.jupiter.api.Test;

class LogNotificationSenderTest { @Test void logsNotification() { new LogNotificationSender().send(new Notification(1L,2L,"T","D","X","R")); } }
