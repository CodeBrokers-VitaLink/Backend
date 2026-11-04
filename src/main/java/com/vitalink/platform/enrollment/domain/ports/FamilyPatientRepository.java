package com.vitalink.platform.enrollment.domain.ports;

import com.vitalink.platform.enrollment.domain.model.commands.PatientEnrollment;
import com.vitalink.platform.enrollment.domain.model.valueobjects.RegisteredFamily;

public interface FamilyPatientRepository {
  String create(PatientEnrollment patient, RegisteredFamily family);
}
