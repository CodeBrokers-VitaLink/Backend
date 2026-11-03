package com.vitalink.platform.iam.interfaces.rest.resources;

import java.time.Instant;

public record SessionResource(
    String token, String tokenType, Instant expiresAt, UserResource user) {}
