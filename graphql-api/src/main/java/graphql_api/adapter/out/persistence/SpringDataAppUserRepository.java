package graphql_api.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

interface SpringDataAppUserRepository extends JpaRepository<AppUserJpaEntity, Long> { Optional<AppUserJpaEntity> findByUsername(String username); boolean existsByUsername(String username); }
