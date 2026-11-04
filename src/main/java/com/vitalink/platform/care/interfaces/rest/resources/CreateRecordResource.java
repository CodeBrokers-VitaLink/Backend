package com.vitalink.platform.care.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record CreateRecordResource(
    @NotBlank String patientId,
    @NotBlank String type,
    @NotNull Object value,
    @Size(max = 30) String unit,
    @Size(max = 2000) String note) {}
