package com.vitalink.platform.care.interfaces.rest;

import com.vitalink.platform.care.application.commandservices.CareCommandService;
import com.vitalink.platform.care.application.queryservices.CareQueryService;
import com.vitalink.platform.care.application.queryservices.CaregiverQueryService;
import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.domain.model.commands.*;
import com.vitalink.platform.care.domain.model.queries.GetPatientByIdQuery;
import com.vitalink.platform.care.interfaces.rest.resources.*;
import com.vitalink.platform.care.interfaces.rest.transform.*;
import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.valueobjects.Role;
import com.vitalink.platform.providers.application.queryservices.ProviderQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Care")
public class CareController {
  private final CareQueryService queries;
  private final CareCommandService commands;
  private final ProviderQueryService providers;
  private final CaregiverQueryService users;
  private final CurrentUserService current;

  public CareController(
      CareQueryService queries,
      CareCommandService commands,
      ProviderQueryService providers,
      CaregiverQueryService users,
      CurrentUserService current) {
    this.queries = queries;
    this.commands = commands;
    this.providers = providers;
    this.users = users;
    this.current = current;
  }

  private PatientResource patient(Patient p) {
    var latest =
        queries.records().stream()
            .filter(r -> r.getPatientId().equals(p.getId()))
            .map(ClinicalRecord::getRecordedAt)
            .max(Comparator.naturalOrder())
            .orElse(null);
    var hospital =
        p.getProviderId() == null
            ? null
            : providers
                .doctor(p.getProviderId())
                .map(
                    d ->
                        d.getClinicId() == null
                            ? null
                            : providers.clinic(d.getClinicId()).map(c -> c.getName()).orElse(null))
                .orElse(null);
    return CareResourceFromEntityAssembler.toResource(p, latest, hospital);
  }

  private RelationshipResource relation(CareRelationship r) {
    return CareResourceFromEntityAssembler.toResource(
        r, providers.doctor(r.getDoctorId()).map(d -> d.getName()).orElse("Médico"));
  }

  @GetMapping("/patients")
  public List<PatientResource> patients() {
    return queries.patients().stream().map(this::patient).toList();
  }

  @GetMapping("/patients/{id}")
  public PatientResource patient(@PathVariable String id) {
    return patient(queries.handle(new GetPatientByIdQuery(id)));
  }

  @PostMapping("/patients")
  @ResponseStatus(HttpStatus.CREATED)
  public PatientResource createPatient(@Valid @RequestBody CreatePatientResource resource) {
    var p = commands.handle(CareCommandFromResourceAssembler.toCommand(resource));
    return CareResourceFromEntityAssembler.toResource(p, null, null);
  }

  @GetMapping("/records")
  public List<RecordResource> records() {
    return queries.records().stream().map(CareResourceFromEntityAssembler::toResource).toList();
  }

  @PostMapping("/records")
  @ResponseStatus(HttpStatus.CREATED)
  public RecordResource createRecord(@Valid @RequestBody CreateRecordResource resource) {
    return CareResourceFromEntityAssembler.toResource(
        commands.handle(CareCommandFromResourceAssembler.toCommand(resource)));
  }

  @GetMapping("/alerts")
  public List<AlertResource> alerts() {
    return queries.alerts().stream().map(CareResourceFromEntityAssembler::toResource).toList();
  }

  @GetMapping("/alerts/{id}")
  public AlertResource alert(@PathVariable String id) {
    return CareResourceFromEntityAssembler.toResource(queries.alert(id));
  }

  @PostMapping("/alerts")
  @ResponseStatus(HttpStatus.CREATED)
  public AlertResource help(@Valid @RequestBody CreateHelpAlertResource resource) {
    return CareResourceFromEntityAssembler.toResource(
        commands.handle(new CreateHelpAlertCommand(resource.patientId())));
  }

  @PatchMapping("/alerts/{id}")
  public AlertResource update(
      @PathVariable String id, @Valid @RequestBody ChangeAlertStatusResource resource) {
    return CareResourceFromEntityAssembler.toResource(
        commands.handle(CareCommandFromResourceAssembler.toCommand(id, resource)));
  }

  @GetMapping("/interventions")
  public List<InterventionResource> interventions() {
    return queries.interventions().stream()
        .map(CareResourceFromEntityAssembler::toResource)
        .toList();
  }

  @GetMapping("/measurementRanges")
  public List<MeasurementRangeResource> ranges() {
    return queries.ranges().stream().map(CareResourceFromEntityAssembler::toResource).toList();
  }

  @GetMapping("/caregivers")
  public List<CaregiverResource> caregivers() {
    var patients = queries.patients();
    var own = current.require();
    var ids =
        patients.stream()
            .flatMap(p -> p.getFamilyIds().stream())
            .collect(java.util.stream.Collectors.toSet());
    if (own.getRole() == Role.FAMILY) ids.add(own.getId());
    return users.profiles(ids).stream()
        .map(
            u ->
                new CaregiverResource(
                    u.getId(),
                    u.getName(),
                    u.getPhone(),
                    u.getEmail(),
                    patients.stream()
                        .filter(p -> p.getFamilyIds().contains(u.getId()))
                        .map(Patient::getId)
                        .toList()))
        .toList();
  }

  @GetMapping("/care-relationships")
  public List<RelationshipResource> relationships() {
    return queries.relationships().stream().map(this::relation).toList();
  }

  @PostMapping("/care-relationships")
  @ResponseStatus(HttpStatus.CREATED)
  public RelationshipResource invite(@Valid @RequestBody InvitePatientResource resource) {
    return relation(commands.handle(new InvitePatientCommand(resource.patientId())));
  }

  @PostMapping("/care-relationships/{id}/{decision}")
  public RelationshipResource respond(@PathVariable String id, @PathVariable String decision) {
    return relation(commands.handle(new RespondToRelationshipCommand(id, decision)));
  }
}
