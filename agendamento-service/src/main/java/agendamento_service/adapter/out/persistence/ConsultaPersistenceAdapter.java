package agendamento_service.adapter.out.persistence;

import agendamento_service.application.port.out.ConsultaRepositoryPort;
import agendamento_service.domain.Consulta;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ConsultaPersistenceAdapter implements ConsultaRepositoryPort {
    private final SpringDataConsultaRepository repository;
    public ConsultaPersistenceAdapter(SpringDataConsultaRepository repository) { this.repository=repository; }
    @Override public Consulta save(Consulta consulta) { return repository.save(new ConsultaJpaEntity(consulta)).toDomain(); }
    @Override public Optional<Consulta> findById(Long id) { return repository.findById(id).map(ConsultaJpaEntity::toDomain); }
    @Override public List<Consulta> findByPatientId(Long id) { return repository.findByPatientIdOrderByDateTimeAsc(id).stream().map(ConsultaJpaEntity::toDomain).toList(); }
    @Override public List<Consulta> findUpcomingByPatientId(Long id, LocalDateTime now) { return repository.findByPatientIdAndDateTimeAfterOrderByDateTimeAsc(id, now).stream().map(ConsultaJpaEntity::toDomain).toList(); }
}
