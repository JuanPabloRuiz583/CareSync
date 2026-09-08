package graphql_api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public class InMemoryAppUserDetailsService implements UserDetailsService {

    private final Map<String, AppUserDetails> users;

    public InMemoryAppUserDetailsService(Map<String, AppUserDetails> users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserDetails template = users.get(username);

        if (template == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new AppUserDetails(
                template.getUsername(),
                template.getPassword(),
                template.getAuthorities(),
                template.getPatientId());
    }
}
