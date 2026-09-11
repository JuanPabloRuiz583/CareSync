package patient_service.application.service;

import patient_service.application.port.in.FindPatientUseCase;
import patient_service.application.port.out.LoadPatientPort;
import patient_service.domain.model.Patient;

public class FindPatientService implements FindPatientUseCase {
    private final LoadPatientPort loadPatientPort;
    public FindPatientService(LoadPatientPort loadPatientPort) { this.loadPatientPort = loadPatientPort; }
    @Override public Patient findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Patient id must be positive");
        return loadPatientPort.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: id=" + id));
    }
}
