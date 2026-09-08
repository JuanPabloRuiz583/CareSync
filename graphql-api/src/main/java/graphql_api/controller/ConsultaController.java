package graphql_api.controller;

import graphql_api.dto.AgendarConsultaInput;
import graphql_api.dto.Consulta;
import graphql_api.dto.EditarConsultaInput;
import graphql_api.security.AppUserDetails;
import graphql_api.service.ConsultaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public List<Consulta> consultasDoPaciente(@Argument Long patientId) {
        checkOwnership(patientId);
        return consultaService.listByPatient(patientId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public List<Consulta> consultasFuturasDoPaciente(@Argument Long patientId) {
        checkOwnership(patientId);
        return consultaService.listUpcomingByPatient(patientId);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ENFERMEIRO')")
    public Consulta agendarConsulta(@Argument("input") AgendarConsultaInput input) {
        return consultaService.create(input.patientId(), input.doctorName(), input.dateTime(), input.reason());
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public Consulta editarConsulta(@Argument("input") EditarConsultaInput input) {
        return consultaService.update(input.id(), input.doctorName(), input.dateTime(), input.reason(), input.status());
    }

    private void checkOwnership(Long patientId) {
        AppUserDetails principal = (AppUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal.isPaciente() && !patientId.equals(principal.getPatientId())) {
            throw new AccessDeniedException("Pacientes só podem visualizar as próprias consultas");
        }
    }
}
