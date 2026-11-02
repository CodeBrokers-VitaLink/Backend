package com.vitalink.platform.iam.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
  DOCTOR,
  FAMILY,
  PATIENT;

  @JsonValue
  public String code() {
    return name().toLowerCase(java.util.Locale.ROOT);
  }

  public static Role from(String value) {
    if (value == null) throw new IllegalArgumentException("role required");
    return valueOf(value.toUpperCase(java.util.Locale.ROOT));
  }
}
