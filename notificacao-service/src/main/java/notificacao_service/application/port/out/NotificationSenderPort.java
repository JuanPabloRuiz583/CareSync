package notificacao_service.application.port.out;

import notificacao_service.domain.Notification;

public interface NotificationSenderPort { void send(Notification notification); }
