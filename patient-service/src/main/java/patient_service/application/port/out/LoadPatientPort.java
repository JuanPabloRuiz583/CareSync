package patient_service.application.port.out;

import patient_service.domain.model.Patient;
import java.util.Optional;

public interface LoadPatientPort { Optional<Patient> findById(Long id); }
