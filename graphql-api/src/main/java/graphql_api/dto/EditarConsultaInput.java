package graphql_api.dto;

public record EditarConsultaInput(Long id, String doctorName, String dateTime, String reason, String status) {
}
