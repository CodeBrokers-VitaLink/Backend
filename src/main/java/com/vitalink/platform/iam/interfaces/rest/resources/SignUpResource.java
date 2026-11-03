package com.vitalink.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record SignUpResource(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Email @Size(max = 200) String email,
    @NotBlank @Size(min = 8, max = 72) String password,
    @NotBlank @Pattern(regexp = "doctor|family|patient") String role,
    @NotBlank @Size(max = 30) String phone,
    @Size(max = 100) String specialty,
    @Size(max = 10) String professionalLicense,
    String practiceType,
    String clinicId,
    String doctorId,
    String serviceType,
    @Min(0) @Max(120) Integer age,
    @Size(max = 100) String location,
    Boolean consent) {}
