package com.vitalink.platform.iam.application.internal.queryservices;

import com.vitalink.platform.iam.application.queryservices.UserQueryService;
import com.vitalink.platform.iam.domain.model.aggregates.User;
import com.vitalink.platform.iam.domain.model.valueobjects.Role;
import com.vitalink.platform.iam.infrastructure.persistence.jpa.UserRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
  private final UserRepository users;

  public UserQueryServiceImpl(UserRepository users) {
    this.users = users;
  }

  public List<User> familyProfiles(Set<String> ids) {
    return users.findAllById(ids).stream().filter(user -> user.getRole() == Role.FAMILY).toList();
  }
}
