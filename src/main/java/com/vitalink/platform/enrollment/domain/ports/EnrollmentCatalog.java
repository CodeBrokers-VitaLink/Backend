package com.vitalink.platform.enrollment.domain.ports;

import com.vitalink.platform.enrollment.domain.model.valueobjects.DoctorAffiliation;
import java.util.Optional;

public interface EnrollmentCatalog {
  Optional<DoctorAffiliation> doctor(String id);
}
