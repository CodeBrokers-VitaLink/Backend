package com.vitalink.platform.care.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

/** Contact information is separate from IAM login credentials. */
@Getter
@Entity
public class CaregiverProfile {
  @Id private String id;
  private String name;
  private String phone;
  private String email;

  protected CaregiverProfile() {}

  public CaregiverProfile(String id, String name, String phone, String email) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.email = email;
  }
}
