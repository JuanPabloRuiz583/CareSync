package graphql_api.service;

import agendamento_service.grpc.AgendamentoServiceGrpc;
import agendamento_service.grpc.CreateConsultaRequest;
import agendamento_service.grpc.ListByPatientRequest;
import agendamento_service.grpc.UpdateConsultaRequest;
import graphql_api.dto.Consulta;
import graphql_api.mapper.ConsultaMapper;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;
import patient_service.grpc.PatientRequest;
import patient_service.grpc.PatientServiceGrpc;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    private final PatientServiceGrpc.PatientServiceBlockingStub patientStub;
    private final AgendamentoServiceGrpc.AgendamentoServiceBlockingStub agendamentoStub;

    public ConsultaService(PatientServiceGrpc.PatientServiceBlockingStub patientStub, AgendamentoServiceGrpc.AgendamentoServiceBlockingStub agendamentoStub) {
        this.patientStub = patientStub;
        this.agendamentoStub = agendamentoStub;
    }

    public List<Consulta> listByPatient(Long patientId) {
        ListByPatientRequest request = ListByPatientRequest.newBuilder().setPatientId(patientId).build();

        return agendamentoStub.listByPatient(request).getConsultasList()
                .stream()
                .map(ConsultaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<Consulta> listUpcomingByPatient(Long patientId) {
        ListByPatientRequest request = ListByPatientRequest.newBuilder().setPatientId(patientId).build();

        return agendamentoStub.listUpcomingByPatient(request).getConsultasList()
                .stream()
                .map(ConsultaMapper::toDto)
                .collect(Collectors.toList());
    }

    public Consulta create(Long patientId, String doctorName, String dateTime, String reason) {
        ensurePatientExists(patientId);
        CreateConsultaRequest request = CreateConsultaRequest.newBuilder()
                .setPatientId(patientId)
                .setDoctorName(doctorName)
                .setDateTime(dateTime)
                .setReason(reason)
                .build();

        return ConsultaMapper.toDto(agendamentoStub.create(request));
    }

    public Consulta update(Long id, String doctorName, String dateTime, String reason, String status) {
        UpdateConsultaRequest request = UpdateConsultaRequest.newBuilder()
                .setId(id)
                .setDoctorName(doctorName)
                .setDateTime(dateTime)
                .setReason(reason)
                .setStatus(status)
                .build();

        return ConsultaMapper.toDto(agendamentoStub.update(request));
    }

    private void ensurePatientExists(Long patientId) {
        PatientRequest request = PatientRequest.newBuilder().setId(patientId).build();

        try {
            patientStub.findById(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new IllegalArgumentException("Patient not found: id=" + patientId);
            }
            throw e;
        }
    }
}
