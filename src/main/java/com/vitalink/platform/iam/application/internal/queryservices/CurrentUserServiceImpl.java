package com.vitalink.platform.iam.application.internal.queryservices;

import com.vitalink.platform.iam.application.queryservices.CurrentUserService;
import com.vitalink.platform.iam.domain.model.aggregates.User;
import com.vitalink.platform.shared.domain.model.exceptions.BusinessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
  public User require() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof User user))
      throw new BusinessException(401, "Debes iniciar sesión.");
    return user;
  }
}
