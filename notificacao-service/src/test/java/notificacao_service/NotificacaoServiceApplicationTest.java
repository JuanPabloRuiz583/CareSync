package notificacao_service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;
class NotificacaoServiceApplicationTest { @Test void delegatesMainToSpring() { String[] args={"--test"}; try(var spring=mockStatic(SpringApplication.class)){ NotificacaoServiceApplication.main(args); spring.verify(()->SpringApplication.run(NotificacaoServiceApplication.class,args)); } } }
