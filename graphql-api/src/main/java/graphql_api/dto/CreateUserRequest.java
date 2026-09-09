package graphql_api.dto;

import graphql_api.domain.UserRole;

public record CreateUserRequest(String username, String password, UserRole role, Long patientId) {
}
