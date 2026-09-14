package graphql_api.config;

import graphql_api.application.port.in.UserManagementUseCase;
import graphql_api.application.port.out.*;
import graphql_api.application.service.UserManagementService;
import org.springframework.context.annotation.*;

@Configuration
public class UserUseCaseConfig {
    @Bean UserManagementUseCase userManagementUseCase(UserRepositoryPort repository, PasswordEncoderPort encoder) { return new UserManagementService(repository, encoder); }
}
