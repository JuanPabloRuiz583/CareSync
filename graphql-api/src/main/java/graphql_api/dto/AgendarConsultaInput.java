package graphql_api.dto;

public record AgendarConsultaInput(Long patientId, String doctorName, String dateTime, String reason) {
}
