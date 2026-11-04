package com.vitalink.platform.enrollment.application.commandservices;

import com.vitalink.platform.enrollment.domain.model.commands.RegisterFamilyCommand;

public interface FamilyEnrollmentCommandService {
  FamilyEnrollmentResult handle(RegisterFamilyCommand command);
}
