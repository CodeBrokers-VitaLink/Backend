package com.vitalink.platform.care.domain.model.commands;

public record ChangeAlertStatusCommand(
    String alertId,
    com.vitalink.platform.care.domain.model.valueobjects.AlertStatus status,
    String note,
    Long expectedVersion) {}
