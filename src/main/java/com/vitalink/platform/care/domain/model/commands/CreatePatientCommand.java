package com.vitalink.platform.care.domain.model.commands;

public record CreatePatientCommand(
    String givenName,
    String familyName,
    int age,
    String location,
    java.util.Set<String> familyIds) {}
