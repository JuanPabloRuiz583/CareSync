package patient_service.adapter.out.persistence;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.Optional;

class PatientPersistenceAdapterTest {
    @Test void mapsFoundAndEmptyEntities() {
        var repository=mock(SpringDataPatientRepository.class); var adapter=new PatientPersistenceAdapter(repository);
        when(repository.findById(1L)).thenReturn(Optional.of(new PatientJpaEntity(1L,"Maria","maria@example.com")));
        assertThat(adapter.findById(1L).orElseThrow().name()).isEqualTo("Maria");
        when(repository.findById(2L)).thenReturn(Optional.empty()); assertThat(adapter.findById(2L)).isEmpty();
        new PatientJpaEntity();
    }
}
