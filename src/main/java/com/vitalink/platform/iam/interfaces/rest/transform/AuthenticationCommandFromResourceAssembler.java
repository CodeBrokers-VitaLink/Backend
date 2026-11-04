package com.vitalink.platform.iam.interfaces.rest.transform;

import com.vitalink.platform.iam.domain.model.commands.*;
import com.vitalink.platform.iam.interfaces.rest.resources.*;

public final class AuthenticationCommandFromResourceAssembler {
  public static SignInCommand toCommand(SignInResource r) {
    return new SignInCommand(r.email(), r.password());
  }

  public static SignUpCommand toCommand(SignUpResource r) {
    return new SignUpCommand(
        r.name(),
        r.email(),
        r.password(),
        r.role(),
        r.phone(),
        r.specialty(),
        r.professionalLicense(),
        r.practiceType(),
        r.clinicId(),
        r.doctorId(),
        r.serviceType(),
        r.age(),
        r.location(),
        r.consent());
  }
}
