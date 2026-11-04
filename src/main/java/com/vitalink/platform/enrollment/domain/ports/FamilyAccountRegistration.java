package com.vitalink.platform.enrollment.domain.ports;

import com.vitalink.platform.enrollment.domain.model.commands.RegisterFamilyCommand;
import com.vitalink.platform.enrollment.domain.model.valueobjects.RegisteredFamily;

public interface FamilyAccountRegistration {
  RegisteredFamily register(RegisterFamilyCommand command);
}
