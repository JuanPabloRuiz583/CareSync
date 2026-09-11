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
        assertThat(c.binding(queue,exchange)).isNotNull(); assertThat(c.rabbitTemplate(mock(ConnectionFactory.class),converter)).isNotNull();
    }
    @Test void buildsUseCase() {
        ConsultaRepositoryPort repo=mock(); ConsultaEventPublisherPort publisher=mock(); var config=new ConsultaUseCaseConfig(); assertThat(config.clock()).isNotNull(); assertThat(config.manageConsultasUseCase(repo,publisher,config.clock())).isNotNull();
    }
}
