package graphql_api.adapter.out.grpc;

import graphql_api.application.port.out.PatientGateway;
import graphql_api.dto.Patient;
import graphql_api.mapper.PatientMapper;
import io.grpc.*;
import org.springframework.stereotype.Component;
import patient_service.grpc.*;

@Component
public class GrpcPatientGateway implements PatientGateway {
    private final PatientServiceGrpc.PatientServiceBlockingStub stub;
    public GrpcPatientGateway(PatientServiceGrpc.PatientServiceBlockingStub stub) { this.stub=stub; }
    @Override public Patient findById(Long id) {
        try { return PatientMapper.toDto(stub.findById(PatientRequest.newBuilder().setId(id).build())); }
        catch (StatusRuntimeException exception) {
            if (exception.getStatus().getCode() == Status.Code.NOT_FOUND) return null;
            throw exception;
        }
    }
}
