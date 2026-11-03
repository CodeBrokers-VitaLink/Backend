package com.vitalink.platform.care.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record CreateHelpAlertResource(@NotBlank String patientId) {}
