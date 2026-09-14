package graphql_api.service;

import graphql_api.application.port.out.*;
import graphql_api.dto.*;
import java.util.List;

public class ConsultaService {
    private final PatientGateway patients;
    private final ConsultaGateway consultas;
    public ConsultaService(PatientGateway patients, ConsultaGateway consultas) { this.patients=patients; this.consultas=consultas; }
    public List<Consulta> listByPatient(Long patientId) { validateId(patientId, "Patient"); return consultas.listByPatient(patientId); }
    public List<Consulta> listUpcomingByPatient(Long patientId) { validateId(patientId, "Patient"); return consultas.listUpcomingByPatient(patientId); }
    public Consulta create(Long patientId, String doctorName, String dateTime, String reason) {
        validateId(patientId, "Patient");
        if (patients.findById(patientId) == null) throw new IllegalArgumentException("Patient not found: id=" + patientId);
        return consultas.create(patientId, doctorName, dateTime, reason);
    }
    public Consulta update(Long id, String doctorName, String dateTime, String reason, String status) { validateId(id, "Consulta"); return consultas.update(id, doctorName, dateTime, reason, status); }
    private static void validateId(Long id, String name) { if (id == null || id <= 0) throw new IllegalArgumentException(name + " id must be positive"); }
}
