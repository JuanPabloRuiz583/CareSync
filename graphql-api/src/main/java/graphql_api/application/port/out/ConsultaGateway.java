package graphql_api.application.port.out;

import graphql_api.dto.Consulta;
import java.util.List;

public interface ConsultaGateway {
    List<Consulta> listByPatient(Long patientId);
    List<Consulta> listUpcomingByPatient(Long patientId);
    Consulta create(Long patientId, String doctorName, String dateTime, String reason);
    Consulta update(Long id, String doctorName, String dateTime, String reason, String status);
}
