package patient_service.adapter.out.persistence;

import org.springframework.stereotype.Component;
import patient_service.application.port.out.LoadPatientPort;
import patient_service.domain.model.Patient;
import java.util.Optional;

@Component
public class PatientPersistenceAdapter implements LoadPatientPort {
    private final SpringDataPatientRepository repository;
    public PatientPersistenceAdapter(SpringDataPatientRepository repository) { this.repository = repository; }
    @Override public Optional<Patient> findById(Long id) {
        return repository.findById(id).map(entity -> new Patient(entity.getId(), entity.getName(), entity.getEmail()));
    }
}
