package agendamento_service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;
class AgendamentoServiceApplicationTest { @Test void delegatesMainToSpring() { String[] args={"--test"}; try(var spring=mockStatic(SpringApplication.class)){ AgendamentoServiceApplication.main(args); spring.verify(()->SpringApplication.run(AgendamentoServiceApplication.class,args)); } } }
