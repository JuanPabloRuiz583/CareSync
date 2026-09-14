package agendamento_service.adapter.out.messaging;

import agendamento_service.application.port.out.ConsultaEventPublisherPort;
import agendamento_service.config.RabbitMQConfig;
import agendamento_service.domain.Consulta;
import agendamento_service.event.ConsultaEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

@Component
public class RabbitConsultaEventPublisher implements ConsultaEventPublisherPort {
    private final RabbitTemplate template;
    public RabbitConsultaEventPublisher(RabbitTemplate template) { this.template=template; }
    @Override public void publish(Consulta c, String type) {
        String key = "CREATED".equals(type) ? RabbitMQConfig.ROUTING_KEY_CREATED : RabbitMQConfig.ROUTING_KEY_UPDATED;
        template.convertAndSend(RabbitMQConfig.EXCHANGE, key, new ConsultaEvent(c.getId(), c.getPatientId(), c.getDoctorName(), c.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), c.getReason(), type));
    }
}
