package graphql_api.adapter.out.security;

import graphql_api.application.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {
    private final PasswordEncoder encoder;
    public BCryptPasswordEncoderAdapter(PasswordEncoder encoder) { this.encoder=encoder; }
    @Override public String encode(String rawPassword) { return encoder.encode(rawPassword); }
}
