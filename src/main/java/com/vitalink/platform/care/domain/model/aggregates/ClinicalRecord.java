package com.vitalink.platform.care.domain.model.aggregates;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
public class ClinicalRecord {
  @Id private String id;
  private String patientId;
  private String type;

  @Column(length = 2000)
  private String valueText;

  private boolean numericValue;
  private String unit;
  private Instant recordedAt;
  private String origin;
  private String providerId;
  private String providerName;

  @Column(length = 2000)
  private String note;

  protected ClinicalRecord() {}

  public ClinicalRecord(
      String id,
      String patientId,
      String type,
      Object value,
      String unit,
      String providerId,
      String providerName,
      String note) {
    if (!(value instanceof String || value instanceof Number) || value.toString().length() > 2000)
      throw new IllegalArgumentException("Invalid measurement value");
    if (value instanceof Number n && !Double.isFinite(n.doubleValue()))
      throw new IllegalArgumentException("Non-finite value");
    this.id = id;
    this.patientId = patientId;
    this.type = type;
    this.valueText = value.toString();
    this.numericValue = value instanceof Number;
    this.unit = unit;
    this.recordedAt = Instant.now();
    this.origin = "professional";
    this.providerId = providerId;
    this.providerName = providerName;
    this.note = note;
  }

  public Object value() {
    return numericValue ? Double.valueOf(valueText) : valueText;
  }

  public static ClinicalRecord simulation(
      String id, String patientId, String type, Object value, String unit, Instant recordedAt) {
    var record =
        new ClinicalRecord(
            id,
            patientId,
            type,
            value,
            unit,
            null,
            null,
            "Medición inicial de simulación ingresada por el familiar.");
    record.origin = "simulation";
    record.recordedAt = recordedAt;
    return record;
  }
}
