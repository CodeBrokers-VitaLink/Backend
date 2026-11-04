package com.vitalink.platform.providers.interfaces.rest.transform;

import com.vitalink.platform.providers.domain.model.aggregates.*;
import com.vitalink.platform.providers.interfaces.rest.resources.*;

public final class ProviderResourceFromEntityAssembler {
  public static ClinicResource toResource(Clinic clinic) {
    return new ClinicResource(clinic.getId(), clinic.getName());
  }

  public static DoctorResource toResource(Doctor doctor) {
    return new DoctorResource(
        doctor.getId(),
        doctor.getName(),
        doctor.getSpecialty(),
        doctor.getPracticeType(),
        doctor.getClinicId(),
        doctor.getVerificationStatus());
  }
}
