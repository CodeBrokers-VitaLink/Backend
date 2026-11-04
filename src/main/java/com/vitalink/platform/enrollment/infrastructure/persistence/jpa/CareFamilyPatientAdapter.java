package com.vitalink.platform.enrollment.infrastructure.persistence.jpa;

import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.infrastructure.persistence.jpa.*;
import com.vitalink.platform.enrollment.domain.model.commands.PatientEnrollment;
import com.vitalink.platform.enrollment.domain.model.valueobjects.RegisteredFamily;
import com.vitalink.platform.enrollment.domain.ports.FamilyPatientRepository;
import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class CareFamilyPatientAdapter implements FamilyPatientRepository {
  private final PatientRepository patients;
  private final ClinicalRecordRepository records;
  private final CareRelationshipRepository relationships;
  private final CaregiverProfileRepository caregivers;

  public CareFamilyPatientAdapter(
      PatientRepository patients,
      ClinicalRecordRepository records,
      CareRelationshipRepository relationships,
      CaregiverProfileRepository caregivers) {
    this.patients = patients;
    this.records = records;
    this.relationships = relationships;
    this.caregivers = caregivers;
  }

  public String create(PatientEnrollment p, RegisteredFamily user) {
    var patient =
        new Patient(
            "P-" + UUID.randomUUID(),
            p.givenName(),
            p.familyName(),
            p.age(),
            p.location(),
            p.doctorId(),
            null,
            Set.of(user.id()));
    patient.useFamilyDisplayName(p.familyDisplayName());
    patients.save(patient);
    caregivers.save(new CaregiverProfile(user.id(), user.name(), user.phone(), user.email()));
    var relation = new CareRelationship("REL-" + UUID.randomUUID(), patient.getId(), p.doctorId());
    relation.respond("accept", user.id());
    relationships.save(relation);
    var v = p.initialVitals();
    var now = Instant.now();
    records.saveAll(
        List.of(
            ClinicalRecord.simulation(
                "R-" + UUID.randomUUID(), patient.getId(), "heartRate", v.heartRate(), "bpm", now),
            ClinicalRecord.simulation(
                "R-" + UUID.randomUUID(), patient.getId(), "oxygen", v.oxygen(), "%", now),
            ClinicalRecord.simulation(
                "R-" + UUID.randomUUID(), patient.getId(), "glucose", v.glucose(), "mg/dL", now),
            ClinicalRecord.simulation(
                "R-" + UUID.randomUUID(),
                patient.getId(),
                "bloodPressure",
                v.bloodPressure(),
                "mmHg",
                now)));
    return patient.getId();
  }
}
