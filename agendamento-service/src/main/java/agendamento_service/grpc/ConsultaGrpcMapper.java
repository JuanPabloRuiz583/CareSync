package agendamento_service.grpc;

import agendamento_service.domain.Consulta;

import java.time.format.DateTimeFormatter;

public class ConsultaGrpcMapper {

    public static ConsultaResponse toResponse(Consulta consulta) {
        return ConsultaResponse.newBuilder()
                .setId(consulta.getId())
                .setPatientId(consulta.getPatientId())
                .setDoctorName(consulta.getDoctorName())
                .setDateTime(consulta.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setStatus(consulta.getStatus().name())
                .setReason(consulta.getReason())
                .build();
    }
}
