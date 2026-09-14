package patient_service.config;

import org.junit.jupiter.api.Test;
import patient_service.application.port.out.LoadPatientPort;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class PatientUseCaseConfigTest {
    @Test void createsUseCase() { LoadPatientPort port=id-> Optional.empty(); assertThat(new PatientUseCaseConfig().findPatientUseCase(port)).isNotNull(); }
}
