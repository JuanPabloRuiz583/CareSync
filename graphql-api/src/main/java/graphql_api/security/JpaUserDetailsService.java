package graphql_api.security;

import graphql_api.domain.AppUser;
import graphql_api.application.port.out.UserRepositoryPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort appUserRepository;

    public JpaUserDetailsService(UserRepositoryPort appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new AppUserDetails(
                user.username(), user.password(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.role().name())), user.patientId());
    }
}
