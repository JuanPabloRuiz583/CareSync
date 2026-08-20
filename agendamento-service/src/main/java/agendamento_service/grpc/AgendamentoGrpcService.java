package agendamento_service.grpc;

import agendamento_service.domain.Consulta;
import agendamento_service.domain.ConsultaStatus;
import agendamento_service.repository.ConsultaRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class AgendamentoGrpcService extends AgendamentoServiceGrpc.AgendamentoServiceImplBase {

    private final ConsultaRepository consultaRepository;

    public AgendamentoGrpcService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void create(CreateConsultaRequest request, StreamObserver<ConsultaResponse> responseObserver) {
        Consulta consulta = new Consulta(
                request.getPatientId(),
                request.getDoctorName(),
                LocalDateTime.parse(request.getDateTime()),
                request.getReason());

        consultaRepository.save(consulta);

        responseObserver.onNext(ConsultaGrpcMapper.toResponse(consulta));
        responseObserver.onCompleted();
    }

    @Override
    public void update(UpdateConsultaRequest request, StreamObserver<ConsultaResponse> responseObserver) {
        Optional<Consulta> found = consultaRepository.findById(request.getId());

        if (found.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Consulta not found: id=" + request.getId())
                    .asRuntimeException());
            return;
        }

        Consulta consulta = found.get();
        consulta.update(
                request.getDoctorName(),
                LocalDateTime.parse(request.getDateTime()),
                request.getReason(),
                ConsultaStatus.valueOf(request.getStatus()));

        consultaRepository.save(consulta);

        responseObserver.onNext(ConsultaGrpcMapper.toResponse(consulta));
        responseObserver.onCompleted();
    }

    @Override
    public void listByPatient(ListByPatientRequest request, StreamObserver<ConsultaListResponse> responseObserver) {
        List<ConsultaResponse> consultas = consultaRepository.findByPatientId(request.getPatientId())
                .stream()
                .map(ConsultaGrpcMapper::toResponse)
                .collect(Collectors.toList());

        responseObserver.onNext(ConsultaListResponse.newBuilder().addAllConsultas(consultas).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listUpcomingByPatient(ListByPatientRequest request, StreamObserver<ConsultaListResponse> responseObserver) {
        List<ConsultaResponse> consultas = consultaRepository
                .findByPatientIdAndDateTimeAfter(request.getPatientId(), LocalDateTime.now())
                .stream()
                .map(ConsultaGrpcMapper::toResponse)
                .collect(Collectors.toList());

        responseObserver.onNext(ConsultaListResponse.newBuilder().addAllConsultas(consultas).build());
        responseObserver.onCompleted();
    }
}
