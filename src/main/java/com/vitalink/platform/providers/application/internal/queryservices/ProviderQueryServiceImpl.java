package com.vitalink.platform.providers.application.internal.queryservices;

import com.vitalink.platform.providers.application.queryservices.ProviderQueryService;
import com.vitalink.platform.providers.domain.model.aggregates.*;
import com.vitalink.platform.providers.domain.model.queries.GetDoctorsQuery;
import com.vitalink.platform.providers.infrastructure.persistence.jpa.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProviderQueryServiceImpl implements ProviderQueryService {
  private final ClinicRepository clinics;
  private final DoctorRepository doctors;

  public ProviderQueryServiceImpl(ClinicRepository clinics, DoctorRepository doctors) {
    this.clinics = clinics;
    this.doctors = doctors;
  }

  public java.util.Optional<Doctor> doctor(String id) {
    return doctors.findById(id);
  }

  public java.util.Optional<Clinic> clinic(String id) {
    return clinics.findById(id);
  }

  public List<Clinic> clinics() {
    return clinics.findAll();
  }

  public List<Doctor> handle(GetDoctorsQuery query) {
    return doctors.findAll().stream()
        .filter(
            d -> query.practiceType() == null || query.practiceType().equals(d.getPracticeType()))
        .filter(d -> query.clinicId() == null || query.clinicId().equals(d.getClinicId()))
        .toList();
  }
}
