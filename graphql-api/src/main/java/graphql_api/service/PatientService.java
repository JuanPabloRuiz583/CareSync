package graphql_api.service;

import graphql_api.dto.Patient;
import graphql_api.mapper.PatientMapper;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;
import patient_service.grpc.PatientRequest;
import patient_service.grpc.PatientServiceGrpc;

@Service
public class PatientService {

    private final PatientServiceGrpc.PatientServiceBlockingStub patientStub;

    public PatientService(PatientServiceGrpc.PatientServiceBlockingStub patientStub) {
        this.patientStub = patientStub;
    }

    public Patient findById(Long id) {
        PatientRequest request = PatientRequest.newBuilder().setId(id).build();

        try {
            return PatientMapper.toDto(patientStub.findById(request));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return null;
            }
            throw e;
        }
    }
}
