package agendamento_service.adapter.out.messaging;

import agendamento_service.config.RabbitMQConfig;
import agendamento_service.domain.*;
import agendamento_service.event.ConsultaEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

class RabbitConsultaEventPublisherTest {
    @Test void publishesCreatedAndUpdated() {
        RabbitTemplate template=mock(); var publisher=new RabbitConsultaEventPublisher(template); var c=new Consulta(1L,2L,"Dr",LocalDateTime.of(2026,1,1,10,0),ConsultaStatus.SCHEDULED,"R");
        publisher.publish(c,"CREATED"); verify(template).convertAndSend(eq(RabbitMQConfig.EXCHANGE),eq(RabbitMQConfig.ROUTING_KEY_CREATED),any(ConsultaEvent.class));
        publisher.publish(c,"UPDATED"); verify(template).convertAndSend(eq(RabbitMQConfig.EXCHANGE),eq(RabbitMQConfig.ROUTING_KEY_UPDATED),any(ConsultaEvent.class));
    }
}
