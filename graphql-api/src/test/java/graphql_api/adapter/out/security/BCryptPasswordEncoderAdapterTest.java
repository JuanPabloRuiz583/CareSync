package graphql_api.adapter.out.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BCryptPasswordEncoderAdapterTest { @Test void delegates() { PasswordEncoder encoder=mock(); when(encoder.encode("raw")).thenReturn("hash"); assertThat(new BCryptPasswordEncoderAdapter(encoder).encode("raw")).isEqualTo("hash"); } }
