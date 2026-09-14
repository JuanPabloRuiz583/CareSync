package patient_service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;
class PatientServiceApplicationTest { @Test void delegatesMainToSpring() { String[] args={"--test"}; try(var spring=mockStatic(SpringApplication.class)){ PatientServiceApplication.main(args); spring.verify(()->SpringApplication.run(PatientServiceApplication.class,args)); } } }
