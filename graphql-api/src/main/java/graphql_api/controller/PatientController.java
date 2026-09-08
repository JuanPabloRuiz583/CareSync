package graphql_api.controller;

import graphql_api.dto.Patient;
import graphql_api.service.PatientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return patientService.findById(id);
    }
}
