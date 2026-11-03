package com.vitalink.platform.care.interfaces.rest.resources;

public record RecordResource(
    String id,
    String patientId,
    String type,
    Object value,
    String unit,
    java.time.Instant recordedAt,
    String origin,
    String providerId,
    String providerName,
    String note) {}
