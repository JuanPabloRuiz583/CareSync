package graphql_api.adapter.out.grpc;

import agendamento_service.grpc.*;
import graphql_api.application.port.out.ConsultaGateway;
import graphql_api.dto.Consulta;
import graphql_api.mapper.ConsultaMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GrpcConsultaGateway implements ConsultaGateway {
    private final AgendamentoServiceGrpc.AgendamentoServiceBlockingStub stub;
    public GrpcConsultaGateway(AgendamentoServiceGrpc.AgendamentoServiceBlockingStub stub) { this.stub=stub; }
    @Override public List<Consulta> listByPatient(Long id) { return stub.listByPatient(patientRequest(id)).getConsultasList().stream().map(ConsultaMapper::toDto).toList(); }
    @Override public List<Consulta> listUpcomingByPatient(Long id) { return stub.listUpcomingByPatient(patientRequest(id)).getConsultasList().stream().map(ConsultaMapper::toDto).toList(); }
    @Override public Consulta create(Long patientId, String doctorName, String dateTime, String reason) { return ConsultaMapper.toDto(stub.create(CreateConsultaRequest.newBuilder().setPatientId(patientId).setDoctorName(doctorName).setDateTime(dateTime).setReason(reason).build())); }
    @Override public Consulta update(Long id, String doctorName, String dateTime, String reason, String status) { return ConsultaMapper.toDto(stub.update(UpdateConsultaRequest.newBuilder().setId(id).setDoctorName(doctorName).setDateTime(dateTime).setReason(reason).setStatus(status).build())); }
    private static ListByPatientRequest patientRequest(Long id) { return ListByPatientRequest.newBuilder().setPatientId(id).build(); }
}
