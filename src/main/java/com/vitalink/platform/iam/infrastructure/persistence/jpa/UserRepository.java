package com.vitalink.platform.iam.infrastructure.persistence.jpa;

import com.vitalink.platform.iam.domain.model.aggregates.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
