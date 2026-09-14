package graphql_api.config;

import graphql_api.application.port.out.*;
import graphql_api.service.*;
import org.springframework.context.annotation.*;

@Configuration
public class ApiUseCaseConfig {
    @Bean PatientService patientService(PatientGateway gateway) { return new PatientService(gateway); }
    @Bean ConsultaService consultaService(PatientGateway patients, ConsultaGateway consultas) { return new ConsultaService(patients, consultas); }
}
