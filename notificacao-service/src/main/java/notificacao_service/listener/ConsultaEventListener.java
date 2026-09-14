package notificacao_service.listener;

import notificacao_service.config.RabbitMQConfig;
import notificacao_service.event.ConsultaEvent;
import notificacao_service.application.port.in.ProcessConsultaEventUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventListener {

    private final ProcessConsultaEventUseCase useCase;
    public ConsultaEventListener(ProcessConsultaEventUseCase useCase) { this.useCase = useCase; }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(ConsultaEvent event) {
        useCase.process(event);
    }
}
