package agendamento_service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

interface SpringDataConsultaRepository extends JpaRepository<ConsultaJpaEntity, Long> {
    List<ConsultaJpaEntity> findByPatientIdOrderByDateTimeAsc(Long patientId);
    List<ConsultaJpaEntity> findByPatientIdAndDateTimeAfterOrderByDateTimeAsc(Long patientId, LocalDateTime now);
}
