package notificacao_service.event;

public record ConsultaEvent(
        Long consultaId,
        Long patientId,
        String doctorName,
        String dateTime,
        String reason,
        String eventType) {
}
