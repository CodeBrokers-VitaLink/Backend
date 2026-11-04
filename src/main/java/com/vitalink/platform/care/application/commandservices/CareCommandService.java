package com.vitalink.platform.care.application.commandservices;

import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.domain.model.commands.*;

public interface CareCommandService {
  CareAlert handle(ChangeAlertStatusCommand command);

  ClinicalRecord handle(CreateRecordCommand command);

  CareAlert handle(CreateHelpAlertCommand command);

  CareRelationship handle(InvitePatientCommand command);

  CareRelationship handle(RespondToRelationshipCommand command);

  Patient handle(CreatePatientCommand command);
}
