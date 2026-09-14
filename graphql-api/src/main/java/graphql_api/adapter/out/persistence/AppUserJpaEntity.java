package graphql_api.adapter.out.persistence;

import graphql_api.domain.*;
import jakarta.persistence.*;

@Entity
@Table(name="app_users")
class AppUserJpaEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=80) private String username;
    @Column(nullable=false, length=100) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private UserRole role;
    @Column(name="patient_id") private Long patientId;
    protected AppUserJpaEntity() {}
    AppUserJpaEntity(AppUser user) { id=user.id(); username=user.username(); password=user.password(); role=user.role(); patientId=user.patientId(); }
    AppUser toDomain() { return new AppUser(id, username, password, role, patientId); }
}
