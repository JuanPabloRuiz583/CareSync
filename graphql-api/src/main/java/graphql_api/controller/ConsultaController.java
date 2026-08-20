package graphql_api.controller;

import graphql_api.dto.AgendarConsultaInput;
import graphql_api.dto.Consulta;
import graphql_api.dto.EditarConsultaInput;
import graphql_api.service.ConsultaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @QueryMapping
    public List<Consulta> consultasDoPaciente(@Argument Long patientId) {
        return consultaService.listByPatient(patientId);
    }

    @QueryMapping
    public List<Consulta> consultasFuturasDoPaciente(@Argument Long patientId) {
        return consultaService.listUpcomingByPatient(patientId);
    }

    @MutationMapping
    public Consulta agendarConsulta(@Argument("input") AgendarConsultaInput input) {
        return consultaService.create(input.patientId(), input.doctorName(), input.dateTime(), input.reason());
    }

    @MutationMapping
    public Consulta editarConsulta(@Argument("input") EditarConsultaInput input) {
        return consultaService.update(input.id(), input.doctorName(), input.dateTime(), input.reason(), input.status());
    }
}
