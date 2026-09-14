package patient_service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPatientRepository extends JpaRepository<PatientJpaEntity, Long> {}
