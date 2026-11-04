package com.vitalink.platform.care.application.internal.commandservices;

import com.vitalink.platform.care.application.commandservices.CareCommandService;
import com.vitalink.platform.care.application.queryservices.*;
import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.domain.model.commands.*;
import com.vitalink.platform.care.domain.model.valueobjects.*;
import com.vitalink.platform.care.infrastructure.persistence.jpa.*;
import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.valueobjects.Role;
import com.vitalink.platform.iam.infrastructure.persistence.jpa.UserRepository;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CareCommandServiceImpl implements CareCommandService {
  private final PatientRepository patients;
  private final ClinicalRecordRepository records;
  private final CareAlertRepository alerts;
  private final InterventionRepository interventions;
  private final CareRelationshipRepository relationships;
  private final CareAccessService access;
  private final CareQueryService queries;
  private final CurrentUserService current;
  private final UserRepository users;

  public CareCommandServiceImpl(
      PatientRepository p,
      ClinicalRecordRepository r,
      CareAlertRepository a,
      InterventionRepository i,
      CareRelationshipRepository c,
      CareAccessService access,
      CareQueryService queries,
      CurrentUserService current,
      UserRepository users) {
    patients = p;
    records = r;
    alerts = a;
    interventions = i;
    relationships = c;
    this.access = access;
    this.queries = queries;
    this.current = current;
    this.users = users;
  }

  private String id(String prefix) {
    return prefix + UUID.randomUUID();
  }

  public CareAlert handle(ChangeAlertStatusCommand c) {
    access.requireDoctor();
    var alert = queries.alert(c.alertId());
    BusinessException.require(
        c.expectedVersion() == null || c.expectedVersion() == alert.getVersion(),
        409,
        "La alerta cambió. Actualiza antes de continuar.");
    var from = alert.transition(c.status(), c.note());
    var user = current.require();
    alerts.saveAndFlush(alert);
    interventions.save(
        new Intervention(
            id("I-"), alert.getId(), user.getId(), user.getName(), from, c.status(), c.note()));
    return alert;
  }

  public ClinicalRecord handle(CreateRecordCommand c) {
    access.requireDoctor();
    access.requirePatient(c.patientId());
    BusinessException.require(
        Set.of("heartRate", "oxygen", "bloodPressure", "glucose", "note", "followUp")
            .contains(c.type()),
        400,
        "Tipo de medición inválido.");
    var user = current.require();
    return records.save(
        new ClinicalRecord(
            id("R-"),
            c.patientId(),
            c.type(),
            c.value(),
            c.unit(),
            user.getId(),
            user.getName(),
            c.note()));
  }

  public CareAlert handle(CreateHelpAlertCommand c) {
    access.requirePatient(c.patientId());
    patients.findForUpdate(c.patientId()).orElseThrow();
    BusinessException.require(
        alerts.findAllByPatientId(c.patientId()).stream()
            .noneMatch(a -> "help".equals(a.getType()) && a.getStatus() != AlertStatus.ATTENDED),
        409,
        "Ya existe una solicitud de ayuda abierta.");
    return alerts.save(new CareAlert(id("A-"), c.patientId(), "help", Severity.HIGH, null));
  }

  public Patient handle(CreatePatientCommand c) {
    access.requireDoctor();
    for (var familyId : c.familyIds())
      BusinessException.require(
          users.findById(familyId).filter(u -> u.getRole() == Role.FAMILY).isPresent(),
          400,
          "Familiar inválido.");
    // Creating a file does not grant the doctor access. An authorized family accepts an invitation.
    BusinessException.require(
        !c.familyIds().isEmpty(), 400, "Debe indicarse al menos un familiar autorizado.");
    return patients.save(
        new Patient(
            id("P-"),
            c.givenName(),
            c.familyName(),
            c.age(),
            c.location(),
            null,
            null,
            c.familyIds()));
  }

  public CareRelationship handle(InvitePatientCommand c) {
    access.requireDoctor();
    var user = current.require();
    var patient =
        patients
            .findForUpdate(c.patientId())
            .orElseThrow(() -> new BusinessException(404, "Paciente no encontrado."));
    BusinessException.require(
        patient.getProviderId() == null, 409, "El paciente ya tiene médico responsable.");
    BusinessException.require(
        relationships.findAllByPatientId(c.patientId()).stream()
            .noneMatch(
                r ->
                    user.getId().equals(r.getDoctorId())
                        && Set.of("pending", "active").contains(r.effectiveStatus())),
        409,
        "La solicitud ya existe.");
    return relationships.save(new CareRelationship(id("REL-"), patient.getId(), user.getId()));
  }

  public CareRelationship handle(RespondToRelationshipCommand c) {
    var relation =
        relationships
            .findById(c.relationshipId())
            .orElseThrow(() -> new BusinessException(404, "Vínculo no encontrado."));
    var patient = patients.findForUpdate(relation.getPatientId()).orElseThrow();
    var user = current.require();
    BusinessException.require(
        access.canAuthorize(patient)
            || "end".equals(c.decision())
                && user.getRole() == Role.DOCTOR
                && user.getId().equals(relation.getDoctorId()),
        403,
        "No puedes autorizar este vínculo.");
    if ("accept".equals(c.decision()))
      BusinessException.require(
          patient.getProviderId() == null, 409, "El paciente ya tiene médico responsable.");
    relation.respond(c.decision(), user.getId());
    if ("accept".equals(c.decision())) {
      patient.assign(relation.getDoctorId());
      for (var other : relationships.findAllByPatientId(patient.getId()))
        if (!other.getId().equals(relation.getId()) && "pending".equals(other.effectiveStatus())) {
          other.respond("reject", user.getId());
          relationships.save(other);
        }
    }
    if ("end".equals(c.decision())
        && Objects.equals(patient.getProviderId(), relation.getDoctorId())) patient.assign(null);
    patients.save(patient);
    return relationships.save(relation);
  }
}
