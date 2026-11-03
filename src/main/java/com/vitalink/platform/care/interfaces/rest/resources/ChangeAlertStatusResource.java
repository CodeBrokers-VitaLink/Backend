package com.vitalink.platform.care.interfaces.rest.resources;

import com.vitalink.platform.care.domain.model.valueobjects.AlertStatus;
import jakarta.validation.constraints.*;

public record ChangeAlertStatusResource(
    @NotNull AlertStatus status, @Size(max = 2000) String note, Long expectedVersion) {}
