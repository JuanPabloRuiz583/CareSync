package graphql_api.config;

import graphql_api.application.port.in.UserManagementUseCase;
import graphql_api.application.port.out.UserRepositoryPort;
import graphql_api.domain.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

@Configuration
public class UserDataSeeder {
    @Bean CommandLineRunner seedUsers(UserRepositoryPort repository, UserManagementUseCase users) {
        return args -> {
            seed(repository, users, "admin1", UserRole.ADMIN, null);
            seed(repository, users, "medico1", UserRole.MEDICO, null);
            seed(repository, users, "enfermeiro1", UserRole.ENFERMEIRO, null);
            seed(repository, users, "paciente1", UserRole.PACIENTE, 1L);
            seed(repository, users, "paciente2", UserRole.PACIENTE, 2L);
        };
    }
    private static void seed(UserRepositoryPort repository, UserManagementUseCase users, String username, UserRole role, Long patientId) {
        if (!repository.existsByUsername(username)) users.create(username, "senha123", role, patientId);
    }
}
