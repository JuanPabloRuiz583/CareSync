package patient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import patient_service.application.port.in.FindPatientUseCase;
import patient_service.application.port.out.LoadPatientPort;
import patient_service.application.service.FindPatientService;

@Configuration
public class PatientUseCaseConfig {
    @Bean FindPatientUseCase findPatientUseCase(LoadPatientPort port) { return new FindPatientService(port); }
}
