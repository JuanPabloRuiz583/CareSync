package patient_service.domain.model;

public record Patient(Long id, String name, String email) {
    public Patient {
        if (id == null || id <= 0) throw new IllegalArgumentException("Patient id must be positive");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Patient name is required");
        if (email == null || email.isBlank() || !email.contains("@")) throw new IllegalArgumentException("Patient email is invalid");
    }
}
