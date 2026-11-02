package com.vitalink.platform.iam.application.commandservices;

import com.vitalink.platform.iam.domain.model.aggregates.User;
import com.vitalink.platform.iam.domain.model.commands.*;
import com.vitalink.platform.shared.application.result.Result;

public interface AuthenticationCommandService {
  Result<User, RegistrationFailure> handle(SignUpCommand command);

  AuthResult handle(SignInCommand command);

  void signOut(String token);
}
