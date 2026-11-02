package com.vitalink.platform.iam.application.commandservices;

import com.vitalink.platform.iam.domain.model.aggregates.User;
import java.time.Instant;

public record AuthResult(User user, String token, Instant expiresAt) {}
