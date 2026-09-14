package agendamento_service.domain;

import java.time.LocalDateTime;

public class Consulta {
    private final Long id;
    private final Long patientId;
    private final String doctorName;
    private final LocalDateTime dateTime;
    private final ConsultaStatus status;
    private final String reason;

    public Consulta(Long patientId, String doctorName, LocalDateTime dateTime, String reason) {
        this(null, patientId, doctorName, dateTime, ConsultaStatus.SCHEDULED, reason);
    }

    public Consulta(Long id, Long patientId, String doctorName, LocalDateTime dateTime, ConsultaStatus status, String reason) {
        if (patientId == null || patientId <= 0) throw new IllegalArgumentException("Patient id must be positive");
        if (doctorName == null || doctorName.isBlank()) throw new IllegalArgumentException("Doctor name is required");
        if (dateTime == null) throw new IllegalArgumentException("Date/time is required");
        if (status == null) throw new IllegalArgumentException("Status is required");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Reason is required");
        this.id = id;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.status = status;
        this.reason = reason;
    }

    public Consulta update(String doctorName, LocalDateTime dateTime, String reason, ConsultaStatus status) {
        return new Consulta(id, patientId, doctorName, dateTime, status, reason);
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ConsultaStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
