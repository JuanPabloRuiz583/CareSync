package agendamento_service.application.port.in;

import agendamento_service.domain.Consulta;
import java.util.List;

public interface ManageConsultasUseCase {
    Consulta create(Long patientId, String doctorName, String dateTime, String reason);
    Consulta update(Long id, String doctorName, String dateTime, String reason, String status);
    List<Consulta> listByPatient(Long patientId);
    List<Consulta> listUpcomingByPatient(Long patientId);
}
