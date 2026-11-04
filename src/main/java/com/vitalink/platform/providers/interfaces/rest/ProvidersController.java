package com.vitalink.platform.providers.interfaces.rest;

import com.vitalink.platform.providers.application.queryservices.ProviderQueryService;
import com.vitalink.platform.providers.domain.model.queries.GetDoctorsQuery;
import com.vitalink.platform.providers.interfaces.rest.resources.*;
import com.vitalink.platform.providers.interfaces.rest.transform.ProviderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Providers")
public class ProvidersController {
  private final ProviderQueryService service;

  public ProvidersController(ProviderQueryService service) {
    this.service = service;
  }

  @GetMapping("/clinics")
  public List<ClinicResource> clinics() {
    return service.clinics().stream().map(ProviderResourceFromEntityAssembler::toResource).toList();
  }

  @GetMapping("/doctors")
  public List<DoctorResource> doctors(
      @RequestParam(required = false) String practiceType,
      @RequestParam(required = false) String clinicId) {
    return service.handle(new GetDoctorsQuery(practiceType, clinicId)).stream()
        .map(ProviderResourceFromEntityAssembler::toResource)
        .toList();
  }
}
