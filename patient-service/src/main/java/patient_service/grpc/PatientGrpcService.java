package patient_service.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import patient_service.application.port.in.FindPatientUseCase;

@GrpcService
public class PatientGrpcService extends PatientServiceGrpc.PatientServiceImplBase {

    private final FindPatientUseCase findPatientUseCase;

    public PatientGrpcService(FindPatientUseCase findPatientUseCase) {
        this.findPatientUseCase = findPatientUseCase;
    }

    @Override
    public void findById(PatientRequest request, StreamObserver<PatientResponse> responseObserver) {
        try {
            var patient = findPatientUseCase.findById(request.getId());
            responseObserver.onNext(PatientGrpcMapper.toResponse(patient));
            responseObserver.onCompleted();
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        }
    }
}
