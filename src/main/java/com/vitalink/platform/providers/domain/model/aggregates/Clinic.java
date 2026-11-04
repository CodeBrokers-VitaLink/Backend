package com.vitalink.platform.providers.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Clinic {
  @Id private String id;

  @Column(nullable = false)
  private String name;

  protected Clinic() {}

  public Clinic(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
