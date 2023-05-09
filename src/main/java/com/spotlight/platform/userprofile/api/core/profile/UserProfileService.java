package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.CommandEnum;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * @author komal3770
 *     <p>User service for business logic
 */
public class UserProfileService {
  private final UserProfileDao userProfileDao;

  @Inject
  public UserProfileService(UserProfileDao userProfileDao) {
    this.userProfileDao = userProfileDao;
  }

  /**
   * Get user profile.
   *
   * @param userId the user id
   * @return the user profile
   */
  public UserProfile get(UserId userId) {
    return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Execute command for UserProfile class & modify properties based on the type of command.
   *
   * @param commandRequest
   * @return the boolean
   */
  public boolean executeCommand(UserCommandRequest commandRequest) {
    UserProfile userProfile = get(commandRequest.getUserId());
    Map<UserProfilePropertyName, UserProfilePropertyValue> properties =
        new HashMap<>(userProfile.userProfileProperties());

    if (commandRequest.getType() == CommandEnum.REPLACE) {
      properties = replaceCommand(properties);
      userProfileDao.put(toUserProfile(commandRequest.getUserId(), properties));
      return true;
    }

    return false;
  }

  private UserProfile toUserProfile(
      UserId userId, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    return new UserProfile(userId, Instant.now(), properties);
  }

  private Map<UserProfilePropertyName, UserProfilePropertyValue> replaceCommand(
      Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    properties
        .entrySet()
        .forEach(
            propEntry -> {
              if (properties.containsKey(propEntry.getKey())) {
                properties.put(propEntry.getKey(), propEntry.getValue());
              }
            });
    return properties;
  }
}
