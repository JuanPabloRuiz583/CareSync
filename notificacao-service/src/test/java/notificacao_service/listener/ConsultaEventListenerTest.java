package notificacao_service.listener;

import notificacao_service.application.port.in.ProcessConsultaEventUseCase;
import notificacao_service.event.ConsultaEvent;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ConsultaEventListenerTest {
    @Test void delegates() { ProcessConsultaEventUseCase useCase=mock(); var event=new ConsultaEvent(1L,2L,"D","X","R","CREATED"); new ConsultaEventListener(useCase).handle(event); verify(useCase).process(event); }
}
