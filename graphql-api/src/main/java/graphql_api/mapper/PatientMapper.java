package graphql_api.mapper;

import graphql_api.dto.Patient;
import patient_service.grpc.PatientResponse;

public class PatientMapper {

    private PatientMapper() {}

    public static Patient toDto(PatientResponse response) {
        return new Patient(response.getId(), response.getName(), response.getEmail());
    }
}
