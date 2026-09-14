package graphql_api.adapter.out.persistence;

import graphql_api.domain.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPersistenceAdapterTest {
    @Test void coversAllRepositoryOperations() { var repo=mock(SpringDataAppUserRepository.class); var domain=new AppUser(1L,"u","p",UserRole.ADMIN,null); var entity=new AppUserJpaEntity(domain); when(repo.save(any())).thenReturn(entity); when(repo.findByUsername("u")).thenReturn(Optional.of(entity)); when(repo.findAll()).thenReturn(List.of(entity)); when(repo.existsByUsername("u")).thenReturn(true); when(repo.existsById(1L)).thenReturn(true); var adapter=new UserPersistenceAdapter(repo); assertThat(adapter.save(domain)).isEqualTo(domain); assertThat(adapter.findByUsername("u")).contains(domain); assertThat(adapter.findAll()).containsExactly(domain); assertThat(adapter.existsByUsername("u")).isTrue(); assertThat(adapter.existsById(1L)).isTrue(); adapter.deleteById(1L); verify(repo).deleteById(1L); new AppUserJpaEntity(); }
}
