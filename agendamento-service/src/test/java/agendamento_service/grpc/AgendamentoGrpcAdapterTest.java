package agendamento_service.grpc;

import agendamento_service.application.port.in.ManageConsultasUseCase;
import agendamento_service.domain.*;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class AgendamentoGrpcAdapterTest {
    private final ManageConsultasUseCase useCase=mock(); private final AgendamentoGrpcService adapter=new AgendamentoGrpcService(useCase);
    private final Consulta consulta=new Consulta(1L,2L,"Dr",LocalDateTime.of(2026,1,2,3,4),ConsultaStatus.SCHEDULED,"R");
    @Test void mapsDomain() { var response=ConsultaGrpcMapper.toResponse(consulta); assertThat(response.getId()).isEqualTo(1); assertThat(response.getPatientId()).isEqualTo(2); assertThat(response.getDoctorName()).isEqualTo("Dr"); assertThat(response.getDateTime()).isEqualTo("2026-01-02T03:04:00"); assertThat(response.getStatus()).isEqualTo("SCHEDULED"); assertThat(response.getReason()).isEqualTo("R"); }
    @Test void servesAllOperations() {
        when(useCase.create(anyLong(),anyString(),anyString(),anyString())).thenReturn(consulta); StreamObserver<ConsultaResponse> one=mock(); adapter.create(CreateConsultaRequest.newBuilder().setPatientId(2).setDoctorName("Dr").setDateTime("2026-01-02T03:04:00").setReason("R").build(),one); verify(one).onCompleted();
        when(useCase.update(anyLong(),anyString(),anyString(),anyString(),anyString())).thenReturn(consulta); StreamObserver<ConsultaResponse> two=mock(); adapter.update(UpdateConsultaRequest.newBuilder().setId(1).setDoctorName("Dr").setDateTime("2026-01-02T03:04:00").setReason("R").setStatus("SCHEDULED").build(),two); verify(two).onCompleted();
        when(useCase.listByPatient(2L)).thenReturn(List.of(consulta)); StreamObserver<ConsultaListResponse> three=mock(); adapter.listByPatient(ListByPatientRequest.newBuilder().setPatientId(2).build(),three); verify(three).onNext(argThat(v->v.getConsultasCount()==1));
        when(useCase.listUpcomingByPatient(2L)).thenReturn(List.of(consulta)); StreamObserver<ConsultaListResponse> four=mock(); adapter.listUpcomingByPatient(ListByPatientRequest.newBuilder().setPatientId(2).build(),four); verify(four).onNext(argThat(v->v.getConsultasCount()==1));
    }
    @Test void mapsNotFoundAndInvalid() {
        when(useCase.update(anyLong(),anyString(),anyString(),anyString(),anyString())).thenThrow(new ConsultaNotFoundException(9L)); StreamObserver<ConsultaResponse> one=mock(); adapter.update(UpdateConsultaRequest.newBuilder().setId(9).build(),one); verify(one).onError(argThat(e->e.getMessage().contains("NOT_FOUND")));
        when(useCase.create(anyLong(),anyString(),anyString(),anyString())).thenThrow(new IllegalArgumentException("bad")); StreamObserver<ConsultaResponse> two=mock(); adapter.create(CreateConsultaRequest.newBuilder().build(),two); verify(two).onError(argThat(e->e.getMessage().contains("INVALID_ARGUMENT")));
    }
}
