package com.vitalink.platform.iam.interfaces.rest.transform;

import com.vitalink.platform.iam.domain.model.aggregates.User;
import com.vitalink.platform.iam.interfaces.rest.resources.UserResource;
import com.vitalink.platform.providers.domain.model.aggregates.Doctor;

public final class UserResourceFromEntityAssembler {
  public static UserResource toResource(User user, Doctor doctor, String hospital) {
    return new UserResource(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole().code(),
        user.getPhone(),
        hospital,
        doctor == null ? null : doctor.getPracticeType(),
        doctor == null ? null : doctor.getClinicId(),
        doctor == null ? null : doctor.getSpecialty(),
        doctor == null ? null : doctor.getProfessionalLicense());
  }
}
