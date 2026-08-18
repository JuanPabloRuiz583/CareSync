package patient_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import patient_service.domain.Patient;
import patient_service.repository.PatientRepository;

@Component
public class PatientDataSeeder implements CommandLineRunner {

    private final PatientRepository patientRepository;

    public PatientDataSeeder(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        patientRepository.save(new Patient("Maria Silva", "maria.silva@example.com"));
        patientRepository.save(new Patient("João Souza", "joao.souza@example.com"));
    }
}
