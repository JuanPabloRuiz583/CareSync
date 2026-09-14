package agendamento_service.grpc;

import agendamento_service.application.port.in.ManageConsultasUseCase;
import agendamento_service.domain.ConsultaNotFoundException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class AgendamentoGrpcService extends AgendamentoServiceGrpc.AgendamentoServiceImplBase {
    private final ManageConsultasUseCase useCase;
    public AgendamentoGrpcService(ManageConsultasUseCase useCase) { this.useCase = useCase; }
    @Override public void create(CreateConsultaRequest request, StreamObserver<ConsultaResponse> observer) {
        execute(observer, () -> ConsultaGrpcMapper.toResponse(useCase.create(request.getPatientId(), request.getDoctorName(), request.getDateTime(), request.getReason())));
    }
    @Override public void update(UpdateConsultaRequest request, StreamObserver<ConsultaResponse> observer) {
        execute(observer, () -> ConsultaGrpcMapper.toResponse(useCase.update(request.getId(), request.getDoctorName(), request.getDateTime(), request.getReason(), request.getStatus())));
    }
    @Override public void listByPatient(ListByPatientRequest request, StreamObserver<ConsultaListResponse> observer) {
        execute(observer, () -> ConsultaListResponse.newBuilder().addAllConsultas(useCase.listByPatient(request.getPatientId()).stream().map(ConsultaGrpcMapper::toResponse).toList()).build());
    }
    @Override public void listUpcomingByPatient(ListByPatientRequest request, StreamObserver<ConsultaListResponse> observer) {
        execute(observer, () -> ConsultaListResponse.newBuilder().addAllConsultas(useCase.listUpcomingByPatient(request.getPatientId()).stream().map(ConsultaGrpcMapper::toResponse).toList()).build());
    }
    private static <T> void execute(StreamObserver<T> observer, java.util.function.Supplier<T> action) {
        try { observer.onNext(action.get()); observer.onCompleted(); }
        catch (ConsultaNotFoundException exception) { observer.onError(Status.NOT_FOUND.withDescription(exception.getMessage()).asRuntimeException()); }
        catch (IllegalArgumentException exception) { observer.onError(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()).asRuntimeException()); }
    }
}
