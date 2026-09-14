package agendamento_service.config;

import agendamento_service.application.port.in.ManageConsultasUseCase;
import agendamento_service.application.port.out.*;
import agendamento_service.application.service.ManageConsultasService;
import org.springframework.context.annotation.*;
import java.time.Clock;

@Configuration
public class ConsultaUseCaseConfig {
    @Bean Clock clock() { return Clock.systemDefaultZone(); }
    @Bean ManageConsultasUseCase manageConsultasUseCase(ConsultaRepositoryPort repository, ConsultaEventPublisherPort publisher, Clock clock) { return new ManageConsultasService(repository, publisher, clock); }
}
