package patient_service.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import patient_service.domain.Patient;
import patient_service.repository.PatientRepository;

import java.util.Optional;

@GrpcService
public class PatientGrpcService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientRepository patientRepository;

    public PatientGrpcService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void findById(PatientRequest request, StreamObserver<PatientResponse> responseObserver) {
        Optional<Patient> patient = patientRepository.findById(request.getId());

        if (patient.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Patient not found: id=" + request.getId())
                    .asRuntimeException());
            return;
        }

        responseObserver.onNext(PatientGrpcMapper.toResponse(patient.get()));
        responseObserver.onCompleted();
    }
}
