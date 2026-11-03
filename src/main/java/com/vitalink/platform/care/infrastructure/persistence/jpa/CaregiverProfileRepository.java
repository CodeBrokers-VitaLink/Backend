package com.vitalink.platform.care.infrastructure.persistence.jpa;

import com.vitalink.platform.care.domain.model.aggregates.CaregiverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverProfileRepository extends JpaRepository<CaregiverProfile, String> {}
