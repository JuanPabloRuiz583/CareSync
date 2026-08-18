package patient_service.grpc;

import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureTestGrpcTransport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestGrpcTransport
@ImportGrpcClients(types = PatientServiceGrpc.PatientServiceBlockingStub.class)
class PatientGrpcServiceTest {

    @Autowired
    private PatientServiceGrpc.PatientServiceBlockingStub patientStub;

    @Test
    void findByIdReturnsExistingPatient() {
        PatientRequest request = PatientRequest.newBuilder().setId(1).build();

        PatientResponse response = patientStub.findById(request);

        assertThat(response.getName()).isEqualTo("Maria Silva");
        assertThat(response.getEmail()).isEqualTo("maria.silva@example.com");
    }

    @Test
    void findByIdThrowsNotFoundForUnknownPatient() {
        PatientRequest request = PatientRequest.newBuilder().setId(99).build();

        assertThatThrownBy(() -> patientStub.findById(request))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessageContaining("NOT_FOUND");
    }
}
