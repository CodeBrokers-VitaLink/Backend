package com.vitalink.platform.care.interfaces.rest.transform;

import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.interfaces.rest.resources.*;
import java.time.Instant;

public final class CareResourceFromEntityAssembler {
  public static PatientResource toResource(Patient p, Instant last, String hospital) {
    return new PatientResource(
        p.getId(),
        p.getGivenName(),
        p.getFamilyName(),
        p.getAge(),
        p.getLocation(),
        p.getProviderId(),
        java.util.Set.copyOf(p.getFamilyIds()),
        last,
        last == null || last.isBefore(Instant.now().minusSeconds(48 * 3600)),
        hospital,
        p.getUserId(),
        p.getFamilyDisplayName());
  }

  public static RecordResource toResource(ClinicalRecord r) {
    return new RecordResource(
        r.getId(),
        r.getPatientId(),
        r.getType(),
        r.value(),
        r.getUnit(),
        r.getRecordedAt(),
        r.getOrigin(),
        r.getProviderId(),
        r.getProviderName(),
        r.getNote());
  }

  public static AlertResource toResource(CareAlert a) {
    return new AlertResource(
        a.getId(),
        a.getPatientId(),
        a.getType(),
        a.getSeverity().name(),
        a.getStatus().name(),
        a.getRaisedAt(),
        a.getRecordId(),
        a.getVersion());
  }

  public static InterventionResource toResource(Intervention i) {
    return new InterventionResource(
        i.getId(),
        i.getAlertId(),
        i.getActorId(),
        i.getActor(),
        i.getRole(),
        i.getFrom().name(),
        i.getTo().name(),
        i.getAt(),
        i.getNote());
  }

  public static RelationshipResource toResource(CareRelationship r, String doctorName) {
    return new RelationshipResource(
        r.getId(),
        r.getPatientId(),
        r.getDoctorId(),
        doctorName,
        r.effectiveStatus(),
        r.getInvitedAt(),
        r.getExpiresAt(),
        r.getAcceptedAt(),
        r.getEndedAt(),
        r.getAuthorizedByUserId());
  }

  public static MeasurementRangeResource toResource(MeasurementRange r) {
    return new MeasurementRangeResource(
        r.getId(),
        r.getUnit(),
        r.getNormalMin(),
        r.getNormalMax(),
        r.getDangerBelow(),
        r.getDangerFrom(),
        r.getDangerAtOrBelow(),
        r.getSystolicMin() == null
            ? null
            : new NumericRangeResource(
                r.getSystolicMin(),
                r.getSystolicMax(),
                r.getSystolicDangerBelow(),
                r.getSystolicDangerFrom(),
                r.getSystolicDangerAtOrBelow()),
        r.getDiastolicMin() == null
            ? null
            : new NumericRangeResource(
                r.getDiastolicMin(),
                r.getDiastolicMax(),
                r.getDiastolicDangerBelow(),
                r.getDiastolicDangerFrom(),
                r.getDiastolicDangerAtOrBelow()));
  }
}
