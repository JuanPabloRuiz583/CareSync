package graphql_api.application.port.out;

import graphql_api.domain.AppUser;
import java.util.*;

public interface UserRepositoryPort {
    AppUser save(AppUser user);
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findAll();
    boolean existsByUsername(String username);
    boolean existsById(Long id);
    void deleteById(Long id);
}
