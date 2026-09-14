package patient_service.adapter.out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
class PatientJpaEntity {
    @Id private Long id;
    @Column(nullable = false, length = 120) private String name;
    @Column(nullable = false, unique = true, length = 180) private String email;
    protected PatientJpaEntity() {}
    PatientJpaEntity(Long id, String name, String email) { this.id = id; this.name = name; this.email = email; }
    Long getId() { return id; }
    String getName() { return name; }
    String getEmail() { return email; }
}
