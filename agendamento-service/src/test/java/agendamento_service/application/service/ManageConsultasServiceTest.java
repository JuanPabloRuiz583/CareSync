package agendamento_service.application.service;

import agendamento_service.application.port.out.*;
import agendamento_service.domain.*;
import org.junit.jupiter.api.*;
import java.time.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageConsultasServiceTest {
    private final ConsultaRepositoryPort repository=mock();
    private final ConsultaEventPublisherPort publisher=mock();
    private final Clock clock=Clock.fixed(Instant.parse("2026-01-01T12:00:00Z"),ZoneOffset.UTC);
    private final ManageConsultasService service=new ManageConsultasService(repository,publisher,clock);
    private final Consulta existing=new Consulta(3L,1L,"Dr A",LocalDateTime.of(2026,2,1,10,0),ConsultaStatus.SCHEDULED,"Checkup");

    @Test void createsAndPublishes() { when(repository.save(any())).thenReturn(existing); assertThat(service.create(1L,"Dr A","2026-02-01T10:00:00","Checkup")).isEqualTo(existing); verify(publisher).publish(existing,"CREATED"); }
    @Test void rejectsPastAndBadDates() {
        assertThatIllegalArgumentException().isThrownBy(()->service.create(1L,"Dr","2025-01-01T00:00:00","R"));
        assertThatIllegalArgumentException().isThrownBy(()->service.create(1L,"Dr","bad","R"));
        assertThatIllegalArgumentException().isThrownBy(()->service.create(1L,"Dr",null,"R"));
    }
    @Test void updatesAndPublishes() { when(repository.findById(3L)).thenReturn(Optional.of(existing)); var updated=new Consulta(3L,1L,"Dr B",LocalDateTime.of(2026,3,1,10,0),ConsultaStatus.COMPLETED,"Done"); when(repository.save(any())).thenReturn(updated); assertThat(service.update(3L,"Dr B","2026-03-01T10:00:00","Done","COMPLETED")).isEqualTo(updated); verify(publisher).publish(updated,"UPDATED"); }
    @Test void rejectsUpdateErrors() {
        assertThatIllegalArgumentException().isThrownBy(()->service.update(null,"D","2026-01-01T00:00:00","R","SCHEDULED"));
        assertThatIllegalArgumentException().isThrownBy(()->service.update(0L,"D","2026-01-01T00:00:00","R","SCHEDULED"));
        when(repository.findById(9L)).thenReturn(Optional.empty()); assertThatThrownBy(()->service.update(9L,"D","2026-01-01T00:00:00","R","SCHEDULED")).isInstanceOf(ConsultaNotFoundException.class);
        when(repository.findById(3L)).thenReturn(Optional.of(existing));
        assertThatIllegalArgumentException().isThrownBy(()->service.update(3L,"D","2026-01-01T00:00:00","R","BAD"));
        assertThatIllegalArgumentException().isThrownBy(()->service.update(3L,"D","2026-01-01T00:00:00","R",null));
    }
    @Test void listsAndValidatesPatient() { when(repository.findByPatientId(1L)).thenReturn(List.of(existing)); when(repository.findUpcomingByPatientId(eq(1L),any())).thenReturn(List.of(existing)); assertThat(service.listByPatient(1L)).containsExactly(existing); assertThat(service.listUpcomingByPatient(1L)).containsExactly(existing); assertThatIllegalArgumentException().isThrownBy(()->service.listByPatient(null)); assertThatIllegalArgumentException().isThrownBy(()->service.listByPatient(0L)); }
    @Test void validatesDomain() {
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(null,"D",LocalDateTime.now(),"R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(0L,"D",LocalDateTime.now(),"R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L,null,LocalDateTime.now(),"R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L," ",LocalDateTime.now(),"R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L,"D",null,"R"));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L,"D",LocalDateTime.now(),null));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L,"D",LocalDateTime.now()," "));
        assertThatIllegalArgumentException().isThrownBy(()->new Consulta(1L,1L,"D",LocalDateTime.now(),null,"R"));
        assertThat(existing.getId()).isEqualTo(3L); assertThat(existing.getPatientId()).isEqualTo(1L); assertThat(existing.getDoctorName()).isEqualTo("Dr A"); assertThat(existing.getStatus()).isEqualTo(ConsultaStatus.SCHEDULED); assertThat(existing.getReason()).isEqualTo("Checkup"); assertThat(existing.getDateTime()).isNotNull();
    }
}
