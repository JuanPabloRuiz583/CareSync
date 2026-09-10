package notificacao_service.listener;

import notificacao_service.config.RabbitMQConfig;
import notificacao_service.event.ConsultaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventListener {

    private static final Logger log = LoggerFactory.getLogger(ConsultaEventListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(ConsultaEvent event) {
        String acao = event.eventType().equals("CREATED") ? "Nova consulta agendada" : "Consulta atualizada";

        log.info("=== LEMBRETE ENVIADO AO PACIENTE ===");
        log.info("{}", acao);
        log.info("Paciente id: {}", event.patientId());
        log.info("Consulta id: {}", event.consultaId());
        log.info("Médico: {}", event.doctorName());
        log.info("Data/hora: {}", event.dateTime());
        log.info("Motivo: {}", event.reason());
        log.info("=====================================");
    }
}
