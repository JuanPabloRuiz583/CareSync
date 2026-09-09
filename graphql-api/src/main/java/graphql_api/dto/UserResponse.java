package graphql_api.dto;

import graphql_api.domain.UserRole;

public record UserResponse(Long id, String username, UserRole role, Long patientId) {
}
