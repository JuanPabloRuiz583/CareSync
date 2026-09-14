package notificacao_service.config;

import notificacao_service.application.port.in.ProcessConsultaEventUseCase;
import notificacao_service.application.port.out.NotificationSenderPort;
import notificacao_service.application.service.ProcessConsultaEventService;
import org.springframework.context.annotation.*;

@Configuration
public class NotificationUseCaseConfig {
    @Bean ProcessConsultaEventUseCase processConsultaEventUseCase(NotificationSenderPort sender) { return new ProcessConsultaEventService(sender); }
}
