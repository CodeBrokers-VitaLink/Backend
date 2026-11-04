package com.vitalink.platform.enrollment.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record RegisterFamilyResource(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Email @Size(max = 200) String email,
    @NotBlank @Size(min = 8, max = 72) String password,
    @NotBlank @Size(max = 30) String phone,
    @NotNull @Valid PatientEnrollmentResource patient) {}
