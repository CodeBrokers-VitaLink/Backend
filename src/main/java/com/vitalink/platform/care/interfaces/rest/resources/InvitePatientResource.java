package com.vitalink.platform.care.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record InvitePatientResource(@NotBlank String patientId) {}
