package graphql_api.controller;

import graphql_api.application.port.in.UserManagementUseCase;
import graphql_api.domain.*;
import graphql_api.dto.*;
import graphql_api.security.AppUserDetails;
import graphql_api.service.*;
import org.junit.jupiter.api.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllersTest {
    @AfterEach void clear() { SecurityContextHolder.clearContext(); }
    private void principal(String role, Long patientId) { var user=new AppUserDetails("u","p",List.of(new SimpleGrantedAuthority("ROLE_"+role)),patientId); SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities())); }
    @Test void patientControllerChecksOwnership() { PatientService service=mock(); var patient=new Patient(1L,"M","m@e.com"); when(service.findById(1L)).thenReturn(patient); var controller=new PatientController(service); principal("PACIENTE",1L); assertThat(controller.patient(1L)).isEqualTo(patient); assertThatThrownBy(()->controller.patient(2L)).isInstanceOf(AccessDeniedException.class); principal("MEDICO",null); controller.patient(1L); }
    @Test void consultaControllerChecksOwnershipAndDelegates() { ConsultaService service=mock(); var c=new Consulta(1L,1L,"D","X","SCHEDULED","R"); when(service.listByPatient(1L)).thenReturn(List.of(c)); when(service.listUpcomingByPatient(1L)).thenReturn(List.of(c)); when(service.create(anyLong(),anyString(),anyString(),anyString())).thenReturn(c); when(service.update(anyLong(),anyString(),anyString(),anyString(),anyString())).thenReturn(c); var controller=new ConsultaController(service); principal("PACIENTE",1L); assertThat(controller.consultasDoPaciente(1L)).hasSize(1); assertThat(controller.consultasFuturasDoPaciente(1L)).hasSize(1); assertThatThrownBy(()->controller.consultasDoPaciente(2L)).isInstanceOf(AccessDeniedException.class); principal("MEDICO",null); controller.consultasDoPaciente(1L); assertThat(controller.agendarConsulta(new AgendarConsultaInput(1L,"D","X","R"))).isEqualTo(c); assertThat(controller.editarConsulta(new EditarConsultaInput(1L,"D","X","R","SCHEDULED"))).isEqualTo(c); }
    @Test void userControllerMaps() { UserManagementUseCase users=mock(); var user=new AppUser(1L,"u","p",UserRole.ADMIN,null); when(users.create(any(),any(),any(),any())).thenReturn(user); when(users.findAll()).thenReturn(List.of(user)); var controller=new UserController(users); assertThat(controller.create(new CreateUserRequest("u","password",UserRole.ADMIN,null)).id()).isEqualTo(1); assertThat(controller.findAll()).hasSize(1); controller.delete(1L); verify(users).delete(1L); }
}
