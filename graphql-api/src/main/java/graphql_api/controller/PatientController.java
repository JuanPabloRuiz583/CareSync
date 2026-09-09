package graphql_api.controller;

import graphql_api.dto.Patient;
import graphql_api.security.AppUserDetails;
import graphql_api.service.PatientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public Patient patient(@Argument Long id) {
        AppUserDetails principal = (AppUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal.isPaciente() && !id.equals(principal.getPatientId())) {
            throw new AccessDeniedException("Pacientes só podem visualizar os próprios dados");
        }

        return patientService.findById(id);
    }
}
