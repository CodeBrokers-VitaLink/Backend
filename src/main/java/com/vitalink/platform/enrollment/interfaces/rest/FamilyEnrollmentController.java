package com.vitalink.platform.enrollment.interfaces.rest;

import com.vitalink.platform.enrollment.application.commandservices.FamilyEnrollmentCommandService;
import com.vitalink.platform.enrollment.interfaces.rest.resources.*;
import com.vitalink.platform.enrollment.interfaces.rest.transform.FamilyEnrollmentAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
@Tag(name = "Enrollment")
public class FamilyEnrollmentController {
  private final FamilyEnrollmentCommandService service;

  public FamilyEnrollmentController(FamilyEnrollmentCommandService service) {
    this.service = service;
  }

  @PostMapping("/families")
  @ResponseStatus(HttpStatus.CREATED)
  public FamilyEnrollmentResource register(@Valid @RequestBody RegisterFamilyResource resource) {
    return FamilyEnrollmentAssembler.toResource(
        service.handle(FamilyEnrollmentAssembler.toCommand(resource)));
  }
}
