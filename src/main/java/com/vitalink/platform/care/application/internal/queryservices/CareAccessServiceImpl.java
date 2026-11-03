package com.vitalink.platform.care.application.internal.queryservices;

import com.vitalink.platform.care.application.queryservices.CareAccessService;
import com.vitalink.platform.care.domain.model.aggregates.Patient;
import com.vitalink.platform.care.infrastructure.persistence.jpa.PatientRepository;
import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.valueobjects.Role;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class CareAccessServiceImpl implements CareAccessService {
  private final CurrentUserService current;
  private final PatientRepository patients;

  public CareAccessServiceImpl(CurrentUserService current, PatientRepository patients) {
    this.current = current;
    this.patients = patients;
  }

  public boolean visible(Patient p) {
    var user = current.require();
    return switch (user.getRole()) {
      case DOCTOR -> user.getId().equals(p.getProviderId());
      case FAMILY -> p.getFamilyIds().contains(user.getId());
      case PATIENT -> user.getId().equals(p.getUserId());
    };
  }

  public boolean canAuthorize(Patient p) {
    var user = current.require();
    return user.getRole() == Role.FAMILY && p.getFamilyIds().contains(user.getId())
        || user.getRole() == Role.PATIENT && user.getId().equals(p.getUserId());
  }

  public Patient requirePatient(String id) {
    return patients
        .findById(id)
        .filter(this::visible)
        .orElseThrow(() -> new BusinessException(404, "Paciente no encontrado."));
  }

  public void requireDoctor() {
    BusinessException.require(
        current.require().getRole() == Role.DOCTOR, 403, "Se requiere un médico autorizado.");
  }
}
