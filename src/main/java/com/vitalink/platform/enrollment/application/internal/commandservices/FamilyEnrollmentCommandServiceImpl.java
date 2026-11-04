package com.vitalink.platform.enrollment.application.internal.commandservices;

import com.vitalink.platform.enrollment.application.commandservices.*;
import com.vitalink.platform.enrollment.domain.model.commands.RegisterFamilyCommand;
import com.vitalink.platform.enrollment.domain.ports.*;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FamilyEnrollmentCommandServiceImpl implements FamilyEnrollmentCommandService {
  private final FamilyAccountRegistration accounts;
  private final EnrollmentCatalog catalog;
  private final FamilyPatientRepository patients;

  public FamilyEnrollmentCommandServiceImpl(
      FamilyAccountRegistration accounts,
      EnrollmentCatalog catalog,
      FamilyPatientRepository patients) {
    this.accounts = accounts;
    this.catalog = catalog;
    this.patients = patients;
  }

  public FamilyEnrollmentResult handle(RegisterFamilyCommand command) {
    var p = command.patient();
    BusinessException.require(
        p != null && p.initialVitals() != null && p.consent(),
        400,
        "Se requieren el paciente, sus signos vitales y la autorización.");
    var doctor =
        catalog
            .doctor(p.doctorId())
            .orElseThrow(() -> new BusinessException(400, "Selecciona un médico válido."));
    BusinessException.require(
        doctor.practiceType().equals(p.serviceType()),
        400,
        "El médico no corresponde a la modalidad elegida.");
    BusinessException.require(
        !"clinic".equals(p.serviceType()) || Objects.equals(doctor.clinicId(), p.clinicId()),
        400,
        "El médico no pertenece a la clínica elegida.");
    var user = accounts.register(command);
    return new FamilyEnrollmentResult(user.id(), patients.create(p, user));
  }
}
