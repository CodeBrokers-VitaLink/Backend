package com.vitalink.platform.care.interfaces.rest.resources;

import jakarta.validation.constraints.*;
import java.util.Set;

public record CreatePatientResource(
    @NotBlank @Size(max = 100) String givenName,
    @NotBlank @Size(max = 100) String familyName,
    @Min(0) @Max(120) int age,
    @NotBlank @Size(max = 100) String location,
    @NotEmpty Set<@NotBlank String> familyIds) {}
