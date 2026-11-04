package com.vitalink.platform.care.interfaces.rest.resources;

public record AlertResource(
    String id,
    String patientId,
    String type,
    String severity,
    String status,
    java.time.Instant raisedAt,
    String recordId,
    long version) {}
