package graphql_api.adapter.out.grpc;

import agendamento_service.grpc.*;
import io.grpc.*;
import org.junit.jupiter.api.Test;
import patient_service.grpc.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrpcGatewaysTest {
    @Test void patientGatewayMapsFoundMissingAndFailure() { var stub=mock(PatientServiceGrpc.PatientServiceBlockingStub.class); var gateway=new GrpcPatientGateway(stub); when(stub.findById(any())).thenReturn(PatientResponse.newBuilder().setId(1).setName("M").setEmail("m@e.com").build()); assertThat(gateway.findById(1L).name()).isEqualTo("M"); reset(stub); when(stub.findById(any())).thenThrow(Status.NOT_FOUND.asRuntimeException()); assertThat(gateway.findById(9L)).isNull(); reset(stub); when(stub.findById(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException()); assertThatThrownBy(()->gateway.findById(1L)).isInstanceOf(StatusRuntimeException.class); }
    @Test void consultaGatewayMapsAllOperations() { var stub=mock(AgendamentoServiceGrpc.AgendamentoServiceBlockingStub.class); var response=ConsultaResponse.newBuilder().setId(1).setPatientId(2).setDoctorName("D").setDateTime("X").setStatus("SCHEDULED").setReason("R").build(); when(stub.listByPatient(any())).thenReturn(ConsultaListResponse.newBuilder().addConsultas(response).build()); when(stub.listUpcomingByPatient(any())).thenReturn(ConsultaListResponse.newBuilder().addConsultas(response).build()); when(stub.create(any())).thenReturn(response); when(stub.update(any())).thenReturn(response); var gateway=new GrpcConsultaGateway(stub); assertThat(gateway.listByPatient(2L)).hasSize(1); assertThat(gateway.listUpcomingByPatient(2L)).hasSize(1); assertThat(gateway.create(2L,"D","X","R").id()).isEqualTo(1); assertThat(gateway.update(1L,"D","X","R","SCHEDULED").id()).isEqualTo(1); }
}
