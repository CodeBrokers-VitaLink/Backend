package com.vitalink.platform.enrollment.domain.model.commands;

import com.vitalink.platform.enrollment.domain.model.valueobjects.InitialVitalSigns;

public record PatientEnrollment(
    String givenName,
    String familyName,
    int age,
    String location,
    String familyDisplayName,
    String serviceType,
    String clinicId,
    String doctorId,
    boolean consent,
    InitialVitalSigns initialVitals) {}
