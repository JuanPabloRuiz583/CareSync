package agendamento_service.application.service;

import agendamento_service.application.port.in.ManageConsultasUseCase;
import agendamento_service.application.port.out.ConsultaEventPublisherPort;
import agendamento_service.application.port.out.ConsultaRepositoryPort;
import agendamento_service.domain.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ManageConsultasService implements ManageConsultasUseCase {
    private final ConsultaRepositoryPort repository;
    private final ConsultaEventPublisherPort publisher;
    private final Clock clock;
    public ManageConsultasService(ConsultaRepositoryPort repository, ConsultaEventPublisherPort publisher, Clock clock) {
        this.repository = repository; this.publisher = publisher; this.clock = clock;
    }
    @Override public Consulta create(Long patientId, String doctorName, String dateTime, String reason) {
        LocalDateTime parsed = parse(dateTime);
        if (!parsed.isAfter(LocalDateTime.now(clock))) throw new IllegalArgumentException("A new appointment must be in the future");
        Consulta saved = repository.save(new Consulta(patientId, doctorName, parsed, reason));
        publisher.publish(saved, "CREATED");
        return saved;
    }
    @Override public Consulta update(Long id, String doctorName, String dateTime, String reason, String status) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Consulta id must be positive");
        Consulta current = repository.findById(id).orElseThrow(() -> new ConsultaNotFoundException(id));
        ConsultaStatus parsedStatus;
        try { parsedStatus = ConsultaStatus.valueOf(status); }
        catch (RuntimeException exception) { throw new IllegalArgumentException("Invalid consulta status: " + status); }
        Consulta saved = repository.save(current.update(doctorName, parse(dateTime), reason, parsedStatus));
        publisher.publish(saved, "UPDATED");
        return saved;
    }
    @Override public List<Consulta> listByPatient(Long patientId) { validatePatientId(patientId); return repository.findByPatientId(patientId); }
    @Override public List<Consulta> listUpcomingByPatient(Long patientId) { validatePatientId(patientId); return repository.findUpcomingByPatientId(patientId, LocalDateTime.now(clock)); }
    private static LocalDateTime parse(String value) {
        try { return LocalDateTime.parse(value); }
        catch (DateTimeParseException | NullPointerException exception) { throw new IllegalArgumentException("Date/time must use ISO-8601 local format"); }
    }
    private static void validatePatientId(Long id) { if (id == null || id <= 0) throw new IllegalArgumentException("Patient id must be positive"); }
}
