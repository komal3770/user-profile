package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
      System.out.println(commandRequest);

      Map<UserProfilePropertyName, UserProfilePropertyValue> properties =
          userProfile.userProfileProperties();
      Map<UserProfilePropertyName, UserProfilePropertyValue> newProperties = new HashMap<>();
      if (properties != null && !properties.isEmpty()) {
        System.out.println(commandRequest.getProperties());
        for (Entry<UserProfilePropertyName, UserProfilePropertyValue> propEntry :
            commandRequest.getProperties().entrySet()) {
          System.out.println(
              "properties key "
                  + propEntry.getKey()
                  + " is present "
                  + properties.containsKey(propEntry.getKey()));
          if (properties.containsKey(propEntry.getKey())) {
            newProperties.put(propEntry.getKey(), propEntry.getValue());
          }
          newProperties.put(propEntry.getKey(), propEntry.getValue());
        }
        UserProfile newUserProfile = new UserProfile(userProfile.userId(), userProfile.latestUpdateTime(), newProperties);

        userProfileDao.put(newUserProfile);
      }
      return true;
    }

    return false;
  }
}
