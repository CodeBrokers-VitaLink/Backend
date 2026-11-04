package com.vitalink.platform.care.interfaces.rest.transform;

import com.vitalink.platform.care.domain.model.commands.*;
import com.vitalink.platform.care.interfaces.rest.resources.*;

public final class CareCommandFromResourceAssembler {
  public static ChangeAlertStatusCommand toCommand(String id, ChangeAlertStatusResource r) {
    return new ChangeAlertStatusCommand(id, r.status(), r.note(), r.expectedVersion());
  }

  public static CreateRecordCommand toCommand(CreateRecordResource r) {
    return new CreateRecordCommand(r.patientId(), r.type(), r.value(), r.unit(), r.note());
  }

  public static CreatePatientCommand toCommand(CreatePatientResource r) {
    return new CreatePatientCommand(
        r.givenName(), r.familyName(), r.age(), r.location(), r.familyIds());
  }
}
