package com.vitalink.platform.care.application.queryservices;

import com.vitalink.platform.care.domain.model.aggregates.Patient;

public interface CareAccessService {
  boolean visible(Patient patient);

  Patient requirePatient(String id);

  void requireDoctor();

  boolean canAuthorize(Patient patient);
}
