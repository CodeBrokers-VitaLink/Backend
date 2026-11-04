package com.vitalink.platform.iam.application.queryservices;

import com.vitalink.platform.iam.domain.model.aggregates.User;
import java.util.*;

public interface UserQueryService {
  List<User> familyProfiles(Set<String> authorizedIds);
}
