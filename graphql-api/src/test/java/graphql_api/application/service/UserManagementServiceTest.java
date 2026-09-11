package graphql_api.application.service;

import graphql_api.application.port.out.*;
import graphql_api.domain.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagementServiceTest {
    private final UserRepositoryPort repo=mock(); private final PasswordEncoderPort encoder=raw->"encoded"; private final UserManagementService service=new UserManagementService(repo,encoder);
    @Test void createsListsAndDeletes() { var saved=new AppUser(1L,"admin","encoded",UserRole.ADMIN,null); when(repo.save(any())).thenReturn(saved); assertThat(service.create("admin","password",UserRole.ADMIN,null)).isEqualTo(saved); when(repo.findAll()).thenReturn(List.of(saved)); assertThat(service.findAll()).containsExactly(saved); when(repo.existsById(1L)).thenReturn(true); service.delete(1L); verify(repo).deleteById(1L); }
    @Test void validatesCreateAndDelete() { when(repo.existsByUsername("taken")).thenReturn(true); assertThatIllegalArgumentException().isThrownBy(()->service.create("taken","password",UserRole.ADMIN,null)); assertThatIllegalArgumentException().isThrownBy(()->service.create("new","short",UserRole.ADMIN,null)); assertThatIllegalArgumentException().isThrownBy(()->service.create("new",null,UserRole.ADMIN,null)); assertThatIllegalArgumentException().isThrownBy(()->service.delete(null)); assertThatIllegalArgumentException().isThrownBy(()->service.delete(0L)); when(repo.existsById(9L)).thenReturn(false); assertThatIllegalArgumentException().isThrownBy(()->service.delete(9L)); }
    @Test void validatesUserDomain() {
        assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,null,"p",UserRole.ADMIN,null)); assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null," ","p",UserRole.ADMIN,null));
        assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u",null,UserRole.ADMIN,null)); assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u"," ",UserRole.ADMIN,null));
        assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u","p",null,null)); assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u","p",UserRole.PACIENTE,null)); assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u","p",UserRole.PACIENTE,0L)); assertThatIllegalArgumentException().isThrownBy(()->new AppUser(null,"u","p",UserRole.ADMIN,1L));
    }
}
