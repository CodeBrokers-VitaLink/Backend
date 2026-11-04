package com.vitalink.platform.care.domain.model.aggregates;

import com.vitalink.platform.care.domain.model.valueobjects.*;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
public class CareAlert {
  @Id private String id;
  private String patientId;
  private String type;
  private String recordId;

  @Enumerated(EnumType.STRING)
  private Severity severity;

  @Enumerated(EnumType.STRING)
  private AlertStatus status;

  private Instant raisedAt;
  @Version private long version;

  protected CareAlert() {}

  public CareAlert(String id, String patientId, String type, Severity severity, String recordId) {
    this.id = id;
    this.patientId = patientId;
    this.type = type;
    this.severity = severity;
    this.recordId = recordId;
    status = AlertStatus.PENDING;
    raisedAt = Instant.now();
  }

  public AlertStatus transition(AlertStatus next, String note) {
    BusinessException.require(
        status != AlertStatus.ATTENDED
            && next != null
            && next != AlertStatus.PENDING
            && next != status,
        409,
        "Transición de alerta no permitida.");
    BusinessException.require(
        next != AlertStatus.ATTENDED || note != null && !note.isBlank(),
        400,
        "La atención requiere una observación.");
    var previous = status;
    status = next;
    return previous;
  }
}
