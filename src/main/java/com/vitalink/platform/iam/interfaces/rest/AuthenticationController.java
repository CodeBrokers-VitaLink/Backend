package com.vitalink.platform.iam.interfaces.rest;

import com.vitalink.platform.iam.application.commandservices.AuthenticationCommandService;
import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.aggregates.User;
import com.vitalink.platform.iam.interfaces.rest.resources.*;
import com.vitalink.platform.iam.interfaces.rest.transform.*;
import com.vitalink.platform.providers.application.queryservices.ProviderQueryService;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "IAM")
public class AuthenticationController {
  private final AuthenticationCommandService commands;
  private final CurrentUserService current;
  private final ProviderQueryService providers;

  public AuthenticationController(
      AuthenticationCommandService commands,
      CurrentUserService current,
      ProviderQueryService providers) {
    this.commands = commands;
    this.current = current;
    this.providers = providers;
  }

  private UserResource resource(User user) {
    var doctor = providers.doctor(user.getId()).orElse(null);
    var hospital =
        doctor != null && doctor.getClinicId() != null
            ? providers.clinic(doctor.getClinicId()).map(c -> c.getName()).orElse(null)
            : null;
    return UserResourceFromEntityAssembler.toResource(user, doctor, hospital);
  }

  @PostMapping("/auth/sign-up")
  public ResponseEntity<?> signUp(@Valid @RequestBody SignUpResource request) {
    BusinessException.require(
        !"family".equals(request.role()),
        400,
        "El registro de familiares requiere datos del paciente y sus signos vitales en"
            + " /enrollment/families.");
    return commands
        .handle(AuthenticationCommandFromResourceAssembler.toCommand(request))
        .fold(
            user -> ResponseEntity.status(201).body(resource(user)),
            failure ->
                ResponseEntity.status(409)
                    .body(
                        ProblemDetail.forStatusAndDetail(
                            HttpStatus.CONFLICT, "Correo o colegiatura ya registrados.")));
  }

  @PostMapping("/auth/sign-in")
  public SessionResource signIn(@Valid @RequestBody SignInResource request) {
    var result = commands.handle(AuthenticationCommandFromResourceAssembler.toCommand(request));
    return new SessionResource(
        result.token(), "Bearer", result.expiresAt(), resource(result.user()));
  }

  @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
  @PostMapping("/auth/sign-out")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void signOut(@RequestHeader("Authorization") String authorization) {
    commands.signOut(authorization.substring(7));
  }

  @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
  @GetMapping("/users/me")
  public UserResource me() {
    return resource(current.require());
  }

  @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
  @GetMapping("/users/{id}")
  public UserResource byId(@PathVariable String id) {
    var user = current.require();
    BusinessException.require(user.getId().equals(id), 404, "Usuario no encontrado.");
    return resource(user);
  }
}
