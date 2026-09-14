package agendamento_service.adapter.out.persistence;

import agendamento_service.domain.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
class ConsultaJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="patient_id", nullable=false) private Long patientId;
    @Column(name="doctor_name", nullable=false, length=120) private String doctorName;
    @Column(name="date_time", nullable=false) private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private ConsultaStatus status;
    @Column(nullable=false, length=500) private String reason;
    protected ConsultaJpaEntity() {}
    ConsultaJpaEntity(Consulta c) { id=c.getId(); patientId=c.getPatientId(); doctorName=c.getDoctorName(); dateTime=c.getDateTime(); status=c.getStatus(); reason=c.getReason(); }
    Consulta toDomain() { return new Consulta(id, patientId, doctorName, dateTime, status, reason); }
}
