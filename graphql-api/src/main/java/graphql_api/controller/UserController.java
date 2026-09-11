package graphql_api.controller;

import graphql_api.application.port.in.UserManagementUseCase;
import graphql_api.domain.AppUser;
import graphql_api.dto.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/users") @PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserManagementUseCase useCase;
    public UserController(UserManagementUseCase useCase) { this.useCase=useCase; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody CreateUserRequest r) { return toResponse(useCase.create(r.username(), r.password(), r.role(), r.patientId())); }
    @GetMapping public List<UserResponse> findAll() { return useCase.findAll().stream().map(UserController::toResponse).toList(); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { useCase.delete(id); }
    static UserResponse toResponse(AppUser u) { return new UserResponse(u.id(), u.username(), u.role(), u.patientId()); }
}
