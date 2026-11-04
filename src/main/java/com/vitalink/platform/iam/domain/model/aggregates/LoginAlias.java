package com.vitalink.platform.iam.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

/** An additional login for an existing account, not a second identity. */
@Getter
@Entity
public class LoginAlias {
  @Id private String id;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String passwordHash;

  protected LoginAlias() {}

  public LoginAlias(String email, String userId, String passwordHash) {
    this.id = new com.vitalink.platform.iam.domain.model.valueobjects.Email(email).value();
    this.userId = userId;
    this.passwordHash = passwordHash;
  }
}
