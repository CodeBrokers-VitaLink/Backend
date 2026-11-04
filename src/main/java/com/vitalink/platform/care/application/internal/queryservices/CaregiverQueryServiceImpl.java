package com.vitalink.platform.care.application.internal.queryservices;

import com.vitalink.platform.care.application.queryservices.CaregiverQueryService;
import com.vitalink.platform.care.domain.model.aggregates.CaregiverProfile;
import com.vitalink.platform.care.infrastructure.persistence.jpa.CaregiverProfileRepository;
import com.vitalink.platform.iam.application.queryservices.UserQueryService;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CaregiverQueryServiceImpl implements CaregiverQueryService {
  private final CaregiverProfileRepository profiles;
  private final UserQueryService users;

  public CaregiverQueryServiceImpl(CaregiverProfileRepository profiles, UserQueryService users) {
    this.profiles = profiles;
    this.users = users;
  }

  public List<CaregiverProfile> profiles(Set<String> authorizedIds) {
    var result = new LinkedHashMap<String, CaregiverProfile>();
    for (var user : users.familyProfiles(authorizedIds))
      result.put(
          user.getId(),
          new CaregiverProfile(user.getId(), user.getName(), user.getPhone(), user.getEmail()));
    for (var contact : profiles.findAllById(authorizedIds)) result.put(contact.getId(), contact);
    return List.copyOf(result.values());
  }
}
