package com.vitalink.platform.care.interfaces.rest.resources;

public record RelationshipResource(
    String id,
    String patientId,
    String doctorId,
    String doctorName,
    String status,
    java.time.Instant invitedAt,
    java.time.Instant expiresAt,
    java.time.Instant acceptedAt,
    java.time.Instant endedAt,
    String authorizedByUserId) {}
