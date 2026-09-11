package notificacao_service.config;

import notificacao_service.application.port.out.NotificationSenderPort;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigsTest {
    @Test void buildsRabbitTopology() { var c=new RabbitMQConfig(); var e=c.consultaExchange(); var q=c.notificacaoQueue(); assertThat(c.binding(q,e)).isNotNull(); assertThat(c.messageConverter()).isNotNull(); var dlx=c.deadLetterExchange(); var dlq=c.deadLetterQueue(); assertThat(c.deadLetterBinding(dlq,dlx)).isNotNull(); }
    @Test void buildsUseCase() { assertThat(new NotificationUseCaseConfig().processConsultaEventUseCase(mock(NotificationSenderPort.class))).isNotNull(); }
}
