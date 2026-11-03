package com.vitalink.platform.care.application.queryservices;

import com.vitalink.platform.care.domain.model.aggregates.*;
import com.vitalink.platform.care.domain.model.queries.GetPatientByIdQuery;
import java.util.List;

public interface CareQueryService {
  List<Patient> patients();

  Patient handle(GetPatientByIdQuery query);

  List<ClinicalRecord> records();

  List<CareAlert> alerts();

  CareAlert alert(String id);

  List<Intervention> interventions();

  List<CareRelationship> relationships();

  List<MeasurementRange> ranges();
}
