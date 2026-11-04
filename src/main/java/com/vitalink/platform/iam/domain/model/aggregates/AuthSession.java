package com.vitalink.platform.iam.domain.model.aggregates;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
public class AuthSession {
  @Id
  @Column(length = 64)
  private String id;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private Instant expiresAt;

  protected AuthSession() {}

  public AuthSession(String tokenHash, String userId, Instant expiresAt) {
    this.id = tokenHash;
    this.userId = userId;
    this.expiresAt = expiresAt;
  }

  public boolean expired() {
    return !expiresAt.isAfter(Instant.now());
  }
}
