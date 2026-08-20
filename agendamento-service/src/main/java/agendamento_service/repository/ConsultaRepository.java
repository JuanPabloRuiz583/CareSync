package agendamento_service.repository;

import agendamento_service.domain.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPatientId(Long patientId);

    List<Consulta> findByPatientIdAndDateTimeAfter(Long patientId, LocalDateTime dateTime);
}
