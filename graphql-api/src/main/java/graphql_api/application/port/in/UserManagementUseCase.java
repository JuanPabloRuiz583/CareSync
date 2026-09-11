package graphql_api.application.port.in;

import graphql_api.domain.*;
import java.util.List;

public interface UserManagementUseCase {
    AppUser create(String username, String password, UserRole role, Long patientId);
    List<AppUser> findAll();
    void delete(Long id);
}
