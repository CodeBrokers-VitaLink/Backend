package com.vitalink.platform.providers.interfaces.rest.resources;

public record DoctorResource(
    String id,
    String name,
    String specialty,
    String practiceType,
    String clinicId,
    String verificationStatus) {}
