package patient_service.application.service;

import org.junit.jupiter.api.Test;
import patient_service.application.port.out.LoadPatientPort;
import patient_service.domain.model.Patient;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class FindPatientServiceTest {
    @Test void findsPatient() { var patient=new Patient(1L,"Maria","maria@example.com"); assertThat(new FindPatientService(id->Optional.of(patient)).findById(1L)).isEqualTo(patient); }
    @Test void rejectsNullAndNonPositiveIds() {
        var service=new FindPatientService(id->Optional.empty());
        assertThatIllegalArgumentException().isThrownBy(()->service.findById(null));
        assertThatIllegalArgumentException().isThrownBy(()->service.findById(0L));
    }
    @Test void reportsMissingPatient() { assertThatIllegalArgumentException().isThrownBy(()->new FindPatientService(id->Optional.empty()).findById(9L)).withMessageContaining("9"); }
    @Test void validatesPatientFields() {
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(null,"A","a@b.com"));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(0L,"A","a@b.com"));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(1L,null,"a@b.com"));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(1L," ","a@b.com"));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(1L,"A",null));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(1L,"A"," "));
        assertThatIllegalArgumentException().isThrownBy(()->new Patient(1L,"A","bad"));
    }
}
