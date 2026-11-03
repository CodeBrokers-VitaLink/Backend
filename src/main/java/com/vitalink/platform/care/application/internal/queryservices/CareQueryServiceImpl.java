package com.vitalink.platform.care.application.internal.queryservices;

import com.vitalink.platform.care.application.queryservices.*;
import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.domain.model.queries.GetPatientByIdQuery;
import com.vitalink.platform.care.infrastructure.persistence.jpa.*;
import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.valueobjects.Role;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CareQueryServiceImpl implements CareQueryService {
  private final PatientRepository patients;
  private final ClinicalRecordRepository records;
  private final CareAlertRepository alerts;
  private final InterventionRepository interventions;
  private final CareRelationshipRepository relationships;
  private final MeasurementRangeRepository ranges;
  private final CareAccessService access;
  private final CurrentUserService current;

  public CareQueryServiceImpl(
      PatientRepository p,
      ClinicalRecordRepository r,
      CareAlertRepository a,
      InterventionRepository i,
      CareRelationshipRepository c,
      MeasurementRangeRepository m,
      CareAccessService access,
      CurrentUserService current) {
    patients = p;
    records = r;
    alerts = a;
    interventions = i;
    relationships = c;
    ranges = m;
    this.access = access;
    this.current = current;
  }

  public List<Patient> patients() {
    return patients.findAll().stream().filter(access::visible).toList();
  }

  public Patient handle(GetPatientByIdQuery query) {
    return access.requirePatient(query.id());
  }

  private Set<String> patientIds() {
    return patients().stream().map(Patient::getId).collect(java.util.stream.Collectors.toSet());
  }

  public List<ClinicalRecord> records() {
    var ids = patientIds();
    return records.findAll().stream()
        .filter(r -> ids.contains(r.getPatientId()))
        .sorted(Comparator.comparing(ClinicalRecord::getRecordedAt).reversed())
        .toList();
  }

  public List<CareAlert> alerts() {
    var ids = patientIds();
    return alerts.findAll().stream().filter(a -> ids.contains(a.getPatientId())).toList();
  }

  public CareAlert alert(String id) {
    var alert =
        alerts.findById(id).orElseThrow(() -> new BusinessException(404, "Alerta no encontrada."));
    access.requirePatient(alert.getPatientId());
    return alert;
  }

  public List<Intervention> interventions() {
    var ids = alerts().stream().map(CareAlert::getId).collect(java.util.stream.Collectors.toSet());
    return interventions.findAll().stream().filter(i -> ids.contains(i.getAlertId())).toList();
  }

  public List<CareRelationship> relationships() {
    var user = current.require();
    var ids = patientIds();
    return relationships.findAll().stream()
        .filter(
            r ->
                user.getRole() == Role.DOCTOR
                    ? user.getId().equals(r.getDoctorId())
                    : ids.contains(r.getPatientId()))
        .toList();
  }

  public List<MeasurementRange> ranges() {
    return ranges.findAll();
  }
}
