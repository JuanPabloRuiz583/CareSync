package graphql_api.dto;

public record Consulta(Long id, Long patientId, String doctorName, String dateTime, String status, String reason) {
}
