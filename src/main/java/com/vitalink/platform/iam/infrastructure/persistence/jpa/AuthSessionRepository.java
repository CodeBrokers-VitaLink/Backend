package com.vitalink.platform.iam.infrastructure.persistence.jpa;

import com.vitalink.platform.iam.domain.model.aggregates.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSessionRepository extends JpaRepository<AuthSession, String> {}
