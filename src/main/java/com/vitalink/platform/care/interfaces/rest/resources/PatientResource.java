package com.vitalink.platform.care.interfaces.rest.resources;

public record PatientResource(
    String id,
    String givenName,
    String familyName,
    int age,
    String location,
    String providerId,
    java.util.Set<String> familyIds,
    java.time.Instant lastRecordAt,
    boolean outdated,
    String hospital,
    String userId,
    String familyDisplayName) {}
