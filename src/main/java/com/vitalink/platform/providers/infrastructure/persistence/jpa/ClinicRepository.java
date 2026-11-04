package com.vitalink.platform.providers.infrastructure.persistence.jpa;

import com.vitalink.platform.providers.domain.model.aggregates.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, String> {}
