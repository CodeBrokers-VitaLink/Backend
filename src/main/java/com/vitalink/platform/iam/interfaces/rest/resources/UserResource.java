package com.vitalink.platform.iam.interfaces.rest.resources;

public record UserResource(
    String id,
    String name,
    String email,
    String role,
    String phone,
    String hospital,
    String practiceType,
    String clinicId,
    String specialty,
    String professionalLicense) {}
