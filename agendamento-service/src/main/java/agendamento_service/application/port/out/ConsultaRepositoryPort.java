package agendamento_service.application.port.out;

import agendamento_service.domain.Consulta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepositoryPort {
    Consulta save(Consulta consulta);
    Optional<Consulta> findById(Long id);
    List<Consulta> findByPatientId(Long patientId);
    List<Consulta> findUpcomingByPatientId(Long patientId, LocalDateTime now);
}
