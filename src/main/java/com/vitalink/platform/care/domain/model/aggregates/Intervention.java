package com.vitalink.platform.care.domain.model.aggregates;

import com.vitalink.platform.care.domain.model.valueobjects.AlertStatus;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
public class Intervention {
  @Id private String id;
  private String alertId;
  private String actorId;
  private String actor;
  private String role;

  @Enumerated(EnumType.STRING)
  @Column(name = "from_status")
  private AlertStatus from;

  @Enumerated(EnumType.STRING)
  @Column(name = "to_status")
  private AlertStatus to;

  @Column(name = "intervened_at")
  private Instant at;

  @Column(length = 2000)
  private String note;

  protected Intervention() {}

  public Intervention(
      String id,
      String alertId,
      String actorId,
      String actor,
      AlertStatus from,
      AlertStatus to,
      String note) {
    this.id = id;
    this.alertId = alertId;
    this.actorId = actorId;
    this.actor = actor;
    this.from = from;
    this.to = to;
    this.note = note;
    this.at = Instant.now();
    this.role = "professional";
  }
}
