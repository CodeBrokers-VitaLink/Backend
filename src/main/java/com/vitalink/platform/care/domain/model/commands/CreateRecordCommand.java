package com.vitalink.platform.care.domain.model.commands;

public record CreateRecordCommand(
    String patientId, String type, Object value, String unit, String note) {}
