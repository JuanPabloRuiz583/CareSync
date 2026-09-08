package graphql_api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private final Long patientId;

    public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long patientId) {
        super(username, password, authorities);
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public boolean isPaciente() {
        return getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_PACIENTE"));
    }
}
