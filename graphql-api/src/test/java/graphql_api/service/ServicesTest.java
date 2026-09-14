package graphql_api.service;

import graphql_api.application.port.out.*;
import graphql_api.dto.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicesTest {
    @Test void patientServiceValidatesAndDelegates() { PatientGateway gateway=mock(); var patient=new Patient(1L,"M","m@e.com"); when(gateway.findById(1L)).thenReturn(patient); var service=new PatientService(gateway); assertThat(service.findById(1L)).isEqualTo(patient); assertThatIllegalArgumentException().isThrownBy(()->service.findById(null)); assertThatIllegalArgumentException().isThrownBy(()->service.findById(0L)); }
    @Test void consultaServiceCoversOperations() { PatientGateway patients=mock(); ConsultaGateway gateway=mock(); var patient=new Patient(1L,"M","m@e.com"); var consulta=new Consulta(2L,1L,"D","X","SCHEDULED","R"); when(patients.findById(1L)).thenReturn(patient); when(gateway.listByPatient(1L)).thenReturn(List.of(consulta)); when(gateway.listUpcomingByPatient(1L)).thenReturn(List.of(consulta)); when(gateway.create(anyLong(),anyString(),anyString(),anyString())).thenReturn(consulta); when(gateway.update(anyLong(),anyString(),anyString(),anyString(),anyString())).thenReturn(consulta); var service=new ConsultaService(patients,gateway); assertThat(service.listByPatient(1L)).hasSize(1); assertThat(service.listUpcomingByPatient(1L)).hasSize(1); assertThat(service.create(1L,"D","X","R")).isEqualTo(consulta); assertThat(service.update(2L,"D","X","R","SCHEDULED")).isEqualTo(consulta); when(patients.findById(1L)).thenReturn(null); assertThatIllegalArgumentException().isThrownBy(()->service.create(1L,"D","X","R")); assertThatIllegalArgumentException().isThrownBy(()->service.listByPatient(null)); assertThatIllegalArgumentException().isThrownBy(()->service.listUpcomingByPatient(0L)); assertThatIllegalArgumentException().isThrownBy(()->service.update(null,"D","X","R","S")); }
}
