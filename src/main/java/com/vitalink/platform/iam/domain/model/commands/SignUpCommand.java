package com.vitalink.platform.iam.domain.model.commands;

public record SignUpCommand(
    String name,
    String email,
    String password,
    String role,
    String phone,
    String specialty,
    String professionalLicense,
    String practiceType,
    String clinicId,
    String doctorId,
    String serviceType,
    Integer age,
    String location,
    Boolean consent) {}
