package graphql_api;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;
class GraphqlApiApplicationTest { @Test void delegatesMainToSpring() { new GraphqlApiApplication(); String[] args={"--test"}; try(var spring=mockStatic(SpringApplication.class)){ GraphqlApiApplication.main(args); spring.verify(()->SpringApplication.run(GraphqlApiApplication.class,args)); } } }
