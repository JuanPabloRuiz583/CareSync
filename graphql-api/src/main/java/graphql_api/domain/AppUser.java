package graphql_api.domain;

public record AppUser(Long id, String username, String password, UserRole role, Long patientId) {
    public AppUser {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password is required");
        if (role == null) throw new IllegalArgumentException("Role is required");
        if (role == UserRole.PACIENTE && (patientId == null || patientId <= 0)) throw new IllegalArgumentException("Patient users require a positive patientId");
        if (role != UserRole.PACIENTE && patientId != null) throw new IllegalArgumentException("Only patient users may have patientId");
    }
}
