package com.vitalink.platform.providers.application.queryservices;

import com.vitalink.platform.providers.domain.model.aggregates.*;
import com.vitalink.platform.providers.domain.model.queries.GetDoctorsQuery;
import java.util.List;

public interface ProviderQueryService {
  java.util.Optional<Doctor> doctor(String id);

  java.util.Optional<Clinic> clinic(String id);

  List<Clinic> clinics();

  List<Doctor> handle(GetDoctorsQuery query);
}
