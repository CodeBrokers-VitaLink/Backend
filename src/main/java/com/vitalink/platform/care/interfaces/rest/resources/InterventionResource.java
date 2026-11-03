package com.vitalink.platform.care.interfaces.rest.resources;

public record InterventionResource(
    String id,
    String alertId,
    String actorId,
    String actor,
    String role,
    String from,
    String to,
    java.time.Instant at,
    String note) {}
