package com.vitalink.platform.enrollment.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record PatientEnrollmentResource(
    @NotBlank @Size(max = 100) String givenName,
    @NotBlank @Size(max = 100) String familyName,
    @NotNull @Min(0) @Max(120) Integer age,
    @NotBlank @Size(max = 100) String location,
    @Size(max = 100) String familyDisplayName,
    @NotBlank @Pattern(regexp = "clinic|independent") String serviceType,
    String clinicId,
    @NotBlank String doctorId,
    @NotNull @AssertTrue Boolean consent,
    @NotNull @Valid InitialVitalSignsResource initialVitals) {}
