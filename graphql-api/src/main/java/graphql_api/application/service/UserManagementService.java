package graphql_api.application.service;

import graphql_api.application.port.in.UserManagementUseCase;
import graphql_api.application.port.out.*;
import graphql_api.domain.*;
import java.util.List;

public class UserManagementService implements UserManagementUseCase {
    private final UserRepositoryPort repository;
    private final PasswordEncoderPort encoder;
    public UserManagementService(UserRepositoryPort repository, PasswordEncoderPort encoder) { this.repository=repository; this.encoder=encoder; }
    @Override public AppUser create(String username, String password, UserRole role, Long patientId) {
        if (repository.existsByUsername(username)) throw new IllegalArgumentException("Username already exists: " + username);
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password must have at least 8 characters");
        return repository.save(new AppUser(null, username, encoder.encode(password), role, patientId));
    }
    @Override public List<AppUser> findAll() { return repository.findAll(); }
    @Override public void delete(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("User id must be positive");
        if (!repository.existsById(id)) throw new IllegalArgumentException("User not found: id=" + id);
        repository.deleteById(id);
    }
}
