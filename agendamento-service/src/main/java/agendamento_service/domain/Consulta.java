package agendamento_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private String doctorName;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private ConsultaStatus status;

    private String reason;

    protected Consulta() {
    }

    public Consulta(Long patientId, String doctorName, LocalDateTime dateTime, String reason) {
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = ConsultaStatus.SCHEDULED;
    }

    public void update(String doctorName, LocalDateTime dateTime, String reason, ConsultaStatus status) {
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = status;
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
