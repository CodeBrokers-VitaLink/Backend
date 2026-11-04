package com.vitalink.platform.iam.domain.model.aggregates;

import com.vitalink.platform.iam.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
public class User {
  @Id private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  private String phone;

  protected User() {}

  public User(String id, String name, String email, String hash, Role role, String phone) {
    this.id = id;
    this.name = name.trim();
    this.email = new Email(email).value();
    this.passwordHash = hash;
    this.role = role;
    this.phone = phone;
  }
}
