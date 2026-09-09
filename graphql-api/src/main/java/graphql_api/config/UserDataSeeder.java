package graphql_api.config;

import graphql_api.domain.AppUserEntity;
import graphql_api.domain.UserRole;
import graphql_api.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        appUserRepository.save(new AppUserEntity("admin1", passwordEncoder.encode("senha123"), UserRole.ADMIN, null));
        appUserRepository.save(new AppUserEntity("medico1", passwordEncoder.encode("senha123"), UserRole.MEDICO, null));
        appUserRepository.save(new AppUserEntity("enfermeiro1", passwordEncoder.encode("senha123"), UserRole.ENFERMEIRO, null));
        appUserRepository.save(new AppUserEntity("paciente1", passwordEncoder.encode("senha123"), UserRole.PACIENTE, 1L));
        appUserRepository.save(new AppUserEntity("paciente2", passwordEncoder.encode("senha123"), UserRole.PACIENTE, 2L));
    }
}
