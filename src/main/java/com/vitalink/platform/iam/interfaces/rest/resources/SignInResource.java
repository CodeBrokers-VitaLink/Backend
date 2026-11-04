package com.vitalink.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record SignInResource(
    @NotBlank @Email String email, @NotBlank @Size(max = 72) String password) {}
