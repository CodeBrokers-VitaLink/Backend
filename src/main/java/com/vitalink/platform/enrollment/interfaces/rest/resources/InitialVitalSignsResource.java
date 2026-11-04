package com.vitalink.platform.enrollment.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record InitialVitalSignsResource(
    @NotNull @DecimalMin("1") @DecimalMax("300") Double heartRate,
    @NotNull @DecimalMin("0") @DecimalMax("100") Double oxygen,
    @NotNull @DecimalMin("1") @DecimalMax("1000") Double glucose,
    @NotNull @DecimalMin("1") @DecimalMax("300") Double systolic,
    @NotNull @DecimalMin("1") @DecimalMax("200") Double diastolic) {}
