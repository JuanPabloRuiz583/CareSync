package graphql_api.config;

import graphql_api.security.AppUserDetails;
import graphql_api.security.InMemoryAppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        Map<String, AppUserDetails> users = Map.of(
                "medico1", new AppUserDetails(
                        "medico1", passwordEncoder.encode("senha123"),
                        List.of(new SimpleGrantedAuthority("ROLE_MEDICO")), null),
                "enfermeiro1", new AppUserDetails(
                        "enfermeiro1", passwordEncoder.encode("senha123"),
                        List.of(new SimpleGrantedAuthority("ROLE_ENFERMEIRO")), null),
                "paciente1", new AppUserDetails(
                        "paciente1", passwordEncoder.encode("senha123"),
                        List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")), 1L),
                "paciente2", new AppUserDetails(
                        "paciente2", passwordEncoder.encode("senha123"),
                        List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")), 2L)
        );

        return new InMemoryAppUserDetailsService(users);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .httpBasic(withDefaults());

        return http.build();
    }
}
