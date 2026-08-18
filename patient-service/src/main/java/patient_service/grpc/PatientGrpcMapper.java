package patient_service.grpc;

import patient_service.domain.Patient;

public class PatientGrpcMapper {

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.newBuilder()
                .setId(patient.getId())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .build();
    }
}
