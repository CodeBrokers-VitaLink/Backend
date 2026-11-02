package com.vitalink.platform.iam.application.commandservices;

public sealed interface RegistrationFailure permits RegistrationFailure.Duplicate {
  record Duplicate() implements RegistrationFailure {}
}
