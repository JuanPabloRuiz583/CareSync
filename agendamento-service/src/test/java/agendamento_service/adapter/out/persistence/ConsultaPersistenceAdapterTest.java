package agendamento_service.adapter.out.persistence;

import agendamento_service.domain.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultaPersistenceAdapterTest {
    @Test void coversPersistenceMappings() {
        var repo=mock(SpringDataConsultaRepository.class); var adapter=new ConsultaPersistenceAdapter(repo); var domain=new Consulta(1L,2L,"Dr",LocalDateTime.now(),ConsultaStatus.SCHEDULED,"R"); var entity=new ConsultaJpaEntity(domain);
        when(repo.save(any())).thenReturn(entity); when(repo.findById(1L)).thenReturn(Optional.of(entity)); when(repo.findById(9L)).thenReturn(Optional.empty()); when(repo.findByPatientIdOrderByDateTimeAsc(2L)).thenReturn(List.of(entity)); when(repo.findByPatientIdAndDateTimeAfterOrderByDateTimeAsc(eq(2L),any())).thenReturn(List.of(entity));
        assertThat(adapter.save(domain).getId()).isEqualTo(1); assertThat(adapter.findById(1L)).isPresent(); assertThat(adapter.findById(9L)).isEmpty(); assertThat(adapter.findByPatientId(2L)).hasSize(1); assertThat(adapter.findUpcomingByPatientId(2L,LocalDateTime.now())).hasSize(1); new ConsultaJpaEntity();
    }
}
