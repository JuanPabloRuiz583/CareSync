package graphql_api.controller;

import graphql_api.domain.AppUserEntity;
import graphql_api.dto.CreateUserRequest;
import graphql_api.dto.UserResponse;
import graphql_api.repository.AppUserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        AppUserEntity user = new AppUserEntity(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.role(),
                request.patientId());

        return toResponse(appUserRepository.save(user));
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return appUserRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        appUserRepository.deleteById(id);
    }

    private UserResponse toResponse(AppUserEntity user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getPatientId());
    }
}
