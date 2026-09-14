package notificacao_service.domain;

public record Notification(Long consultaId, Long patientId, String title, String doctorName, String dateTime, String reason) {
    public Notification {
        if (consultaId == null || consultaId <= 0) throw new IllegalArgumentException("Consulta id must be positive");
        if (patientId == null || patientId <= 0) throw new IllegalArgumentException("Patient id must be positive");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Notification title is required");
    }
}
