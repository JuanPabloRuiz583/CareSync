package graphql_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;
import patient_service.grpc.PatientServiceGrpc;

@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "patient-service", types = PatientServiceGrpc.PatientServiceBlockingStub.class)
public class GrpcClientConfig {
}
