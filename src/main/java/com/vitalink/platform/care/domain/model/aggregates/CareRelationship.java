package com.vitalink.platform.care.domain.model.aggregates;

import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
public class CareRelationship {
  @Id private String id;
  private String patientId;
  private String doctorId;
  private String status;
  private Instant invitedAt;
  private Instant expiresAt;
  private Instant acceptedAt;
  private Instant endedAt;
  private String authorizedByUserId;
  @Version private long version;

  protected CareRelationship() {}

  public CareRelationship(String id, String patientId, String doctorId) {
    this.id = id;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.status = "pending";
    this.invitedAt = Instant.now();
    this.expiresAt = invitedAt.plusSeconds(7 * 86400);
  }

  public String effectiveStatus() {
    return "pending".equals(status) && !expiresAt.isAfter(Instant.now()) ? "expired" : status;
  }

  public void respond(String decision, String userId) {
    if ("end".equals(decision)) {
      BusinessException.require("active".equals(status), 409, "El vínculo no está activo.");
      status = "ended";
      endedAt = Instant.now();
      return;
    }
    BusinessException.require(
        "pending".equals(effectiveStatus()), 409, "La invitación ya no está pendiente.");
    BusinessException.require(
        java.util.Set.of("accept", "reject").contains(decision), 400, "Decisión inválida.");
    authorizedByUserId = userId;
    if ("accept".equals(decision)) {
      status = "active";
      acceptedAt = Instant.now();
    } else status = "rejected";
  }
}
