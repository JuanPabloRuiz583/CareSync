package notificacao_service.application.service;

import notificacao_service.application.port.out.NotificationSenderPort;
import notificacao_service.domain.Notification;
import notificacao_service.event.ConsultaEvent;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessConsultaEventServiceTest {
    private final NotificationSenderPort sender=mock(); private final ProcessConsultaEventService service=new ProcessConsultaEventService(sender);
    private ConsultaEvent event(String type) { return new ConsultaEvent(1L,2L,"Dr","2026-01-01T10:00:00","R",type); }
    @Test void processesCreatedAndUpdated() { service.process(event("CREATED")); verify(sender).send(argThat(n->n.title().equals("Nova consulta agendada"))); service.process(event("UPDATED")); verify(sender).send(argThat(n->n.title().equals("Consulta atualizada"))); }
    @Test void rejectsNullAndUnknown() { assertThatIllegalArgumentException().isThrownBy(()->service.process(null)); assertThatIllegalArgumentException().isThrownBy(()->service.process(event("OTHER"))); }
    @Test void validatesNotification() {
        assertThatIllegalArgumentException().isThrownBy(()->new Notification(null,1L,"T","D","X","R")); assertThatIllegalArgumentException().isThrownBy(()->new Notification(0L,1L,"T","D","X","R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Notification(1L,null,"T","D","X","R")); assertThatIllegalArgumentException().isThrownBy(()->new Notification(1L,0L,"T","D","X","R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Notification(1L,1L,null,"D","X","R")); assertThatIllegalArgumentException().isThrownBy(()->new Notification(1L,1L," ","D","X","R"));
    }
}
