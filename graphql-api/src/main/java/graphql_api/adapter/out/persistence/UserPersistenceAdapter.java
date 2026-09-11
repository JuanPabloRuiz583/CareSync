package graphql_api.adapter.out.persistence;

import graphql_api.application.port.out.UserRepositoryPort;
import graphql_api.domain.AppUser;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final SpringDataAppUserRepository repository;
    public UserPersistenceAdapter(SpringDataAppUserRepository repository) { this.repository=repository; }
    @Override public AppUser save(AppUser user) { return repository.save(new AppUserJpaEntity(user)).toDomain(); }
    @Override public Optional<AppUser> findByUsername(String username) { return repository.findByUsername(username).map(AppUserJpaEntity::toDomain); }
    @Override public List<AppUser> findAll() { return repository.findAll().stream().map(AppUserJpaEntity::toDomain).toList(); }
    @Override public boolean existsByUsername(String username) { return repository.existsByUsername(username); }
    @Override public boolean existsById(Long id) { return repository.existsById(id); }
    @Override public void deleteById(Long id) { repository.deleteById(id); }
}
