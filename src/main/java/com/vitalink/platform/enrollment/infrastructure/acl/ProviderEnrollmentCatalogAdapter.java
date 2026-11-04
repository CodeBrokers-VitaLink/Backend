package com.vitalink.platform.enrollment.infrastructure.acl;

import com.vitalink.platform.enrollment.domain.model.valueobjects.DoctorAffiliation;
import com.vitalink.platform.enrollment.domain.ports.EnrollmentCatalog;
import com.vitalink.platform.providers.application.queryservices.ProviderQueryService;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProviderEnrollmentCatalogAdapter implements EnrollmentCatalog {
  private final ProviderQueryService providers;

  public ProviderEnrollmentCatalogAdapter(ProviderQueryService providers) {
    this.providers = providers;
  }

  public Optional<DoctorAffiliation> doctor(String id) {
    return providers
        .doctor(id)
        .map(d -> new DoctorAffiliation(d.getId(), d.getPracticeType(), d.getClinicId()));
  }
}
