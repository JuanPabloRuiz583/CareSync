package graphql_api.config;

import agendamento_service.grpc.AgendamentoServiceGrpc;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;
import patient_service.grpc.PatientServiceGrpc;

@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "patient-service", types = PatientServiceGrpc.PatientServiceBlockingStub.class)
@ImportGrpcClients(target = "agendamento-service", types = AgendamentoServiceGrpc.AgendamentoServiceBlockingStub.class)
public class GrpcClientConfig {
}
