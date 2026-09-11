package graphql_api.security;

import graphql_api.application.port.out.UserRepositoryPort;
import graphql_api.domain.*;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityComponentsTest {
    private UserDetails details() { return new AppUserDetails("user","password",List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")),1L); }
    @AfterEach void clear() { org.springframework.security.core.context.SecurityContextHolder.clearContext(); }
    @Test void userDetailsExposePatient() { var patient=(AppUserDetails)details(); assertThat(patient.getPatientId()).isEqualTo(1); assertThat(patient.isPaciente()).isTrue(); assertThat(new AppUserDetails("m","p",List.of(new SimpleGrantedAuthority("ROLE_MEDICO")),null).isPaciente()).isFalse(); }
    @Test void jpaUserDetailsLoadsAndRejects() { UserRepositoryPort repo=mock(); when(repo.findByUsername("user")).thenReturn(Optional.of(new AppUser(1L,"user","hash",UserRole.PACIENTE,1L))); var service=new JpaUserDetailsService(repo); assertThat(service.loadUserByUsername("user").getAuthorities()).extracting("authority").containsExactly("ROLE_PACIENTE"); when(repo.findByUsername("none")).thenReturn(Optional.empty()); assertThatThrownBy(()->service.loadUserByUsername("none")).isInstanceOf(UsernameNotFoundException.class); }
    @Test void jwtGeneratesValidatesAndExpires() { String secret="oN3/gkdGOWKQ2zvz3oKZnTJUU3VtSAlPTUDdKypyLPw="; var jwt=new JwtService(secret,60000); String token=jwt.generateToken(details()); assertThat(jwt.extractUsername(token)).isEqualTo("user"); assertThat(jwt.isTokenValid(token,details())).isTrue(); assertThat(jwt.isTokenValid(token,new User("other","p",List.of()))).isFalse(); var expired=new JwtService(secret,-1); assertThat(expired.isTokenValid(expired.generateToken(details()),details())).isFalse(); }
    @Test void filterHandlesAbsentValidInvalidAndMalformedTokens() throws Exception {
        JwtService jwt=mock(); UserDetailsService users=mock(); var filter=new JwtAuthenticationFilter(jwt,users); HttpServletRequest request=mock(); HttpServletResponse response=mock(); FilterChain chain=mock();
        filter.doFilterInternal(request,response,chain); verify(chain).doFilter(request,response);
        when(request.getHeader("Authorization")).thenReturn("Basic x"); filter.doFilterInternal(request,response,chain);
        when(request.getHeader("Authorization")).thenReturn("Bearer good"); when(jwt.extractUsername("good")).thenReturn("user"); when(users.loadUserByUsername("user")).thenReturn(details()); when(jwt.isTokenValid(eq("good"),any())).thenReturn(true); filter.doFilterInternal(request,response,chain); assertThat(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        filter.doFilterInternal(request,response,chain);
        clear(); when(jwt.isTokenValid(eq("good"),any())).thenReturn(false); filter.doFilterInternal(request,response,chain); assertThat(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()).isNull();
        when(request.getHeader("Authorization")).thenReturn("Bearer bad"); when(jwt.extractUsername("bad")).thenThrow(new JwtException("bad")); filter.doFilterInternal(request,response,chain); assertThat(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()).isNull();
        when(request.getHeader("Authorization")).thenReturn("Bearer removed"); when(jwt.extractUsername("removed")).thenReturn("removed"); when(users.loadUserByUsername("removed")).thenThrow(new UsernameNotFoundException("removed")); filter.doFilterInternal(request,response,chain); assertThat(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
