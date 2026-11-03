package com.vitalink.platform.iam.domain.model.valueobjects;

public record Email(String value) {
  public Email {
    if (value == null || !value.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
      throw new IllegalArgumentException("Invalid email");
    value = value.trim().toLowerCase(java.util.Locale.ROOT);
  }
}
