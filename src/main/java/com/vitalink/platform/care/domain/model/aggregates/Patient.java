package com.vitalink.platform.care.domain.model.aggregates;

import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;

@Getter
@Entity
public class Patient {
  @Id private String id;
  private String givenName;
  private String familyName;
  private String familyDisplayName;
  private int age;
  private String location;
  private String providerId;
  private String userId;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<String> familyIds = new HashSet<>();

  @Version private long version;

  protected Patient() {}

  public Patient(
      String id,
      String givenName,
      String familyName,
      int age,
      String location,
      String providerId,
      String userId,
      Set<String> familyIds) {
    if (age < 0 || age > 120 || givenName == null || givenName.isBlank())
      throw new IllegalArgumentException("Invalid patient");
    this.id = id;
    this.givenName = givenName;
    this.familyName = familyName;
    this.age = age;
    this.location = location;
    this.providerId = providerId;
    this.userId = userId;
    this.familyIds = new HashSet<>(familyIds);
  }

  public void assign(String doctorId) {
    this.providerId = doctorId;
  }

  public void useFamilyDisplayName(String displayName) {
    if (displayName != null && displayName.length() > 100)
      throw new IllegalArgumentException("Nombre familiar demasiado largo");
    this.familyDisplayName =
        displayName == null || displayName.isBlank() ? null : displayName.trim();
  }
}
