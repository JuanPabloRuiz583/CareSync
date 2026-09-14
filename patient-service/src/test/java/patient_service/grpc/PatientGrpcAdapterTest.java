package patient_service.grpc;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import patient_service.application.port.in.FindPatientUseCase;
import patient_service.domain.model.Patient;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class PatientGrpcAdapterTest {
    @Test void mapsAndReturnsPatient() {
        var patient=new Patient(1L,"Maria","maria@example.com");
        PatientResponse mapped=PatientGrpcMapper.toResponse(patient);
        assertThat(mapped.getId()).isEqualTo(1); assertThat(mapped.getName()).isEqualTo("Maria");
        FindPatientUseCase useCase=mock(); when(useCase.findById(1L)).thenReturn(patient);
        StreamObserver<PatientResponse> observer=mock(); new PatientGrpcService(useCase).findById(PatientRequest.newBuilder().setId(1).build(),observer);
        verify(observer).onNext(mapped); verify(observer).onCompleted(); verify(observer,never()).onError(any());
    }
    @Test void mapsErrorToNotFound() {
        FindPatientUseCase useCase=mock(); when(useCase.findById(9L)).thenThrow(new IllegalArgumentException("missing"));
        StreamObserver<PatientResponse> observer=mock(); new PatientGrpcService(useCase).findById(PatientRequest.newBuilder().setId(9).build(),observer);
        verify(observer).onError(argThat(e->e.getMessage().contains("NOT_FOUND")));
    }
}
