package graphql_api.controller;

import graphql_api.dto.*;
import graphql_api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Test void authenticatesAndHandlesFailure() { AuthenticationManager manager=mock(); UserDetailsService users=mock(); JwtService jwt=mock(); var details=new User("u","p",List.of()); when(users.loadUserByUsername("u")).thenReturn(details); when(jwt.generateToken(details)).thenReturn("token"); var controller=new AuthController(manager,users,jwt); assertThat(controller.login(new LoginRequest("u","p")).token()).isEqualTo("token"); assertThat(controller.handleAuthenticationFailure(new BadCredentialsException("bad"))).containsEntry("error","Usuário ou senha inválidos"); }
}
