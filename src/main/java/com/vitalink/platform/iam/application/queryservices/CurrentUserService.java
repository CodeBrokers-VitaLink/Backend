package com.vitalink.platform.iam.application.queryservices;

import com.vitalink.platform.iam.domain.model.aggregates.User;

public interface CurrentUserService {
  User require();
}
