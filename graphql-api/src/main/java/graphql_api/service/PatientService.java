package graphql_api.service;

import graphql_api.application.port.out.PatientGateway;
import graphql_api.dto.Patient;

public class PatientService {
    private final PatientGateway gateway;
    public PatientService(PatientGateway gateway) { this.gateway=gateway; }
    public Patient findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Patient id must be positive");
        return gateway.findById(id);
    }
}
