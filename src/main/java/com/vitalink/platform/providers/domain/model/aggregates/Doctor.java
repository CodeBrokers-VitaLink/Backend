package com.vitalink.platform.providers.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(
    uniqueConstraints =
        @UniqueConstraint(name = "uk_doctors_license", columnNames = "professional_license"))
public class Doctor {
  @Id private String id;
  private String name;
  private String specialty;

  @Column(nullable = false)
  private String professionalLicense;

  private String practiceType;
  private String clinicId;
  private String verificationStatus;

  protected Doctor() {}

  public Doctor(
      String id,
      String name,
      String specialty,
      String license,
      String practiceType,
      String clinicId) {
    if (!java.util.Set.of("clinic", "independent").contains(practiceType))
      throw new IllegalArgumentException("Invalid practice");
    if ("clinic".equals(practiceType) && (clinicId == null || clinicId.isBlank()))
      throw new IllegalArgumentException("Clinic required");
    this.id = id;
    this.name = name;
    this.specialty = specialty;
    this.professionalLicense = license;
    this.practiceType = practiceType;
    this.clinicId = "clinic".equals(practiceType) ? clinicId : null;
    this.verificationStatus = "pending";
  }
}
