package com.vitalink.platform.iam.infrastructure.persistence.jpa;

import com.vitalink.platform.iam.domain.model.aggregates.LoginAlias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAliasRepository extends JpaRepository<LoginAlias, String> {}
