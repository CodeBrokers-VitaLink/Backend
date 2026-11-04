package com.vitalink.platform.enrollment.infrastructure.acl;

import com.vitalink.platform.enrollment.domain.model.commands.RegisterFamilyCommand;
import com.vitalink.platform.enrollment.domain.model.valueobjects.RegisteredFamily;
import com.vitalink.platform.enrollment.domain.ports.FamilyAccountRegistration;
import com.vitalink.platform.iam.application.commandservices.AuthenticationCommandService;
import com.vitalink.platform.iam.domain.model.commands.SignUpCommand;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class IamFamilyAccountAdapter implements FamilyAccountRegistration {
  private final AuthenticationCommandService accounts;

  public IamFamilyAccountAdapter(AuthenticationCommandService accounts) {
    this.accounts = accounts;
  }

  public RegisteredFamily register(RegisterFamilyCommand c) {
    var result =
        accounts.handle(
            new SignUpCommand(
                c.name(),
                c.email(),
                c.password(),
                "family",
                c.phone(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));
    return result.fold(
        u -> new RegisteredFamily(u.getId(), u.getName(), u.getPhone(), u.getEmail()),
        failure -> {
          throw new BusinessException(409, "El correo ya está registrado.");
        });
  }
}
