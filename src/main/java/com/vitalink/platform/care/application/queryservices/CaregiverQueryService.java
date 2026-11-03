package com.vitalink.platform.care.application.queryservices;

import com.vitalink.platform.care.domain.model.aggregates.CaregiverProfile;
import java.util.List;
import java.util.Set;

public interface CaregiverQueryService {
  List<CaregiverProfile> profiles(Set<String> authorizedIds);
}
