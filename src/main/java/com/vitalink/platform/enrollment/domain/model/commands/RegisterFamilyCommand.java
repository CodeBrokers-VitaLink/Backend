package com.vitalink.platform.enrollment.domain.model.commands;

public record RegisterFamilyCommand(
    String name, String email, String password, String phone, PatientEnrollment patient) {}
