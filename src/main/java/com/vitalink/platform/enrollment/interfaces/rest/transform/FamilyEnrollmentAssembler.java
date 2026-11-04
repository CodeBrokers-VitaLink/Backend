package com.vitalink.platform.enrollment.interfaces.rest.transform;

import com.vitalink.platform.enrollment.application.commandservices.FamilyEnrollmentResult;
import com.vitalink.platform.enrollment.domain.model.commands.*;
import com.vitalink.platform.enrollment.domain.model.valueobjects.InitialVitalSigns;
import com.vitalink.platform.enrollment.interfaces.rest.resources.*;

public final class FamilyEnrollmentAssembler {
  public static RegisterFamilyCommand toCommand(RegisterFamilyResource r) {
    var p = r.patient();
    var v = p.initialVitals();
    return new RegisterFamilyCommand(
        r.name(),
        r.email(),
        r.password(),
        r.phone(),
        new PatientEnrollment(
            p.givenName(),
            p.familyName(),
            p.age(),
            p.location(),
            p.familyDisplayName(),
            p.serviceType(),
            p.clinicId(),
            p.doctorId(),
            p.consent(),
            new InitialVitalSigns(
                v.heartRate(), v.oxygen(), v.glucose(), v.systolic(), v.diastolic())));
  }

  public static FamilyEnrollmentResource toResource(FamilyEnrollmentResult r) {
    return new FamilyEnrollmentResource(r.userId(), r.patientId());
  }
}
