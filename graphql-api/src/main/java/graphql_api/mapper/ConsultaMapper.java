package graphql_api.mapper;

import agendamento_service.grpc.ConsultaResponse;
import graphql_api.dto.Consulta;

public class ConsultaMapper {

    private ConsultaMapper() {}

    public static Consulta toDto(ConsultaResponse response) {
        return new Consulta(
                response.getId(),
                response.getPatientId(),
                response.getDoctorName(),
                response.getDateTime(),
                response.getStatus(),
                response.getReason());
    }
}
