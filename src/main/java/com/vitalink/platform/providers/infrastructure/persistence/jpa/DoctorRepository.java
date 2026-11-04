package com.vitalink.platform.providers.infrastructure.persistence.jpa;

import com.vitalink.platform.providers.domain.model.aggregates.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
  boolean existsByProfessionalLicense(String license);
}
