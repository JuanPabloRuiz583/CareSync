package graphql_api.config;

import graphql_api.application.port.out.*;
import graphql_api.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;
import graphql_api.security.JwtAuthenticationFilter;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationTest {
    @Test void createsUseCasesAndSecurityBeans() throws Exception { new GrpcClientConfig(); PatientGateway patients=mock(); ConsultaGateway consultas=mock(); var api=new ApiUseCaseConfig(); assertThat(api.patientService(patients)).isNotNull(); assertThat(api.consultaService(patients,consultas)).isNotNull(); UserRepositoryPort users=mock(); PasswordEncoderPort encoder=mock(); assertThat(new UserUseCaseConfig().userManagementUseCase(users,encoder)).isNotNull(); var security=new SecurityConfig(); assertThat(security.passwordEncoder()).isNotNull(); assertThat(security.userDetailsService(users)).isNotNull(); AuthenticationConfiguration auth=mock(); AuthenticationManager manager=mock(); when(auth.getAuthenticationManager()).thenReturn(manager); assertThat(security.authenticationManager(auth)).isSameAs(manager); }
    @Test void seedsOnlyMissingUsers() throws Exception { UserRepositoryPort repo=mock(); graphql_api.application.port.in.UserManagementUseCase users=mock(); when(repo.existsByUsername("admin1")).thenReturn(true); new UserDataSeeder().seedUsers(repo,users).run(); verify(users,times(4)).create(anyString(),eq("senha123"),any(UserRole.class),nullable(Long.class)); }
    @SuppressWarnings({"rawtypes","unchecked"})
    @Test void buildsSecurityFilterChain() throws Exception {
        HttpSecurity http=mock(HttpSecurity.class); DefaultSecurityFilterChain chain=mock();
        when(http.csrf(any())).thenAnswer(i->{ ((Customizer)i.getArgument(0)).customize(mock(CsrfConfigurer.class,RETURNS_DEEP_STUBS)); return http; });
        when(http.sessionManagement(any())).thenAnswer(i->{ ((Customizer)i.getArgument(0)).customize(mock(SessionManagementConfigurer.class,RETURNS_DEEP_STUBS)); return http; });
        when(http.authorizeHttpRequests(any())).thenAnswer(i->{ ((Customizer)i.getArgument(0)).customize(mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class,RETURNS_DEEP_STUBS)); return http; });
        when(http.exceptionHandling(any())).thenAnswer(i->{ ((Customizer)i.getArgument(0)).customize(mock(ExceptionHandlingConfigurer.class,RETURNS_DEEP_STUBS)); return http; });
        when(http.addFilterBefore(any(),any())).thenReturn(http); when(http.build()).thenReturn(chain);
        assertThat(new SecurityConfig().filterChain(http,mock(JwtAuthenticationFilter.class))).isSameAs(chain);
    }
}
