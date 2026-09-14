package agendamento_service.config;

import agendamento_service.application.port.out.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigsTest {
    @Test void buildsRabbitComponents() {
        var c=new RabbitMQConfig(); var exchange=c.consultaExchange(); var queue=c.notificacaoQueue(); var converter=c.messageConverter();
        assertThat(queue.getArguments()).containsEntry("x-dead-letter-exchange", RabbitMQConfig.DEAD_LETTER_EXCHANGE)
                .containsEntry("x-dead-letter-routing-key", RabbitMQConfig.DEAD_LETTER_QUEUE);
        assertThat(c.binding(queue,exchange)).isNotNull(); assertThat(c.rabbitTemplate(mock(ConnectionFactory.class),converter)).isNotNull();
    }
    @Test void buildsUseCase() {
        ConsultaRepositoryPort repo=mock(); ConsultaEventPublisherPort publisher=mock(); var config=new ConsultaUseCaseConfig(); assertThat(config.clock()).isNotNull(); assertThat(config.manageConsultasUseCase(repo,publisher,config.clock())).isNotNull();
    }
}
