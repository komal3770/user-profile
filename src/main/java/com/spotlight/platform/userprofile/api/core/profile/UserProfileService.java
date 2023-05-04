package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
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

  public boolean put(UserProfile userProfile) {
    System.out.println(
        "Userprofile isPresent() "
            + userProfileDao.get(userProfile.userId()).isPresent()
            + " :: "
            + userProfile.userId());
    if (userProfileDao.get(userProfile.userId()).isPresent()) return false;
    userProfileDao.put(userProfile);
    return true;
  }

  public boolean executeCommand(UserCommandRequest commandRequest) {
    UserProfile userProfile = get(UserId.valueOf(commandRequest.getUserId()));
    if (commandRequest.getType().equalsIgnoreCase("replace")) {
      Map<UserProfilePropertyName, UserProfilePropertyValue> properties =
          userProfile.userProfileProperties();
      if (properties != null && !properties.isEmpty()) {
        commandRequest.getProperties().entrySet().stream()
            .forEach(
                propEntry -> {
                  if (properties.containsKey(propEntry.getKey())) {
                    UserProfilePropertyName propertyName =
                        UserProfilePropertyName.valueOf(propEntry.getKey());
                    UserProfilePropertyValue propertyValue =
                        UserProfilePropertyValue.valueOf(propEntry.getValue());
                    properties.put(propertyName, propertyValue);
                  }
                });
      }
      return true;
    }

    return false;
  }
}
