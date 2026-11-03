package com.vitalink.platform.care.infrastructure.persistence.jpa;

import com.vitalink.platform.care.domain.model.aggregates.ClinicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, String> {
  java.util.List<ClinicalRecord> findAllByPatientIdOrderByRecordedAtDesc(String patientId);
}
