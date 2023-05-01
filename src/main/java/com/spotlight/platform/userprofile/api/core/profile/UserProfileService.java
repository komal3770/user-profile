package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class UserProfileService {
  private final UserProfileDao userProfileDao;

  @Inject
  public UserProfileService(UserProfileDao userProfileDao) {
    this.userProfileDao = userProfileDao;
  }

  public UserProfile get(UserId userId) {
    return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
  }

  public void put() {
    Map<UserProfilePropertyName, UserProfilePropertyValue> userProfileProperties = new HashMap<>();

    userProfileDao.put(new UserProfile(new UserId("AAS123"), Instant.now(), userProfileProperties));
  }
}
