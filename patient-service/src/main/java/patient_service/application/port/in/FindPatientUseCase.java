package patient_service.application.port.in;

import patient_service.domain.model.Patient;

public interface FindPatientUseCase { Patient findById(Long id); }
