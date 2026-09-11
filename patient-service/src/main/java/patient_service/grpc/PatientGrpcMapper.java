package patient_service.grpc;

import patient_service.domain.model.Patient;

public class PatientGrpcMapper {

    private PatientGrpcMapper() {
    }

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.newBuilder()
                .setId(patient.id())
                .setName(patient.name())
                .setEmail(patient.email())
                .build();
    }
}
