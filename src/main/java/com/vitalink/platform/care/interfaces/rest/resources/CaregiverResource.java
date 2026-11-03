package com.vitalink.platform.care.interfaces.rest.resources;

public record CaregiverResource(
    String id, String name, String phone, String email, java.util.List<String> patientIds) {}
