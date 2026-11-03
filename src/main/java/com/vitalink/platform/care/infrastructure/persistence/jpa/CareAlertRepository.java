package com.vitalink.platform.care.infrastructure.persistence.jpa;

import com.vitalink.platform.care.domain.model.aggregates.CareAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareAlertRepository extends JpaRepository<CareAlert, String> {
  java.util.List<CareAlert> findAllByPatientId(String patientId);
}
