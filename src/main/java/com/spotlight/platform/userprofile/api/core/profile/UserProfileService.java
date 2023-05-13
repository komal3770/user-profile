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
import java.util.*;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

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
   * @param commandRequest request to execute command
   * @return the boolean
   */
  public Optional<UserProfile> executeCommand(UserCommandRequest commandRequest) {
    UserProfile userProfile = get(commandRequest.getUserId());
    Map<UserProfilePropertyName, UserProfilePropertyValue> properties =
        new HashMap<>(userProfile.userProfileProperties());
    for (Map.Entry<UserProfilePropertyName, UserProfilePropertyValue> entry :
        commandRequest.getProperties().entrySet()) {
      UserProfilePropertyName key = entry.getKey();
      UserProfilePropertyValue value = entry.getValue();
      if (properties.containsKey(key)) {
        if(commandRequest.getType() == null){
          throw new BadRequestException("Invalid command type");
        }
        if (commandRequest.getType() == CommandEnum.REPLACE) {
          properties.put(key, value);
        } else if (commandRequest.getType() == CommandEnum.INCREMENT && value != null) {
          int val = Integer.parseInt(value.getValue().toString());
          val = Integer.parseInt(properties.get(key).getValue().toString()) + val;
          properties.put(key, UserProfilePropertyValue.valueOf(val));
        } else if (commandRequest.getType() == CommandEnum.COLLECT && value != null) {
          System.out.println(value.getValue());
          List<Object> list = new ArrayList<>(List.of(value.getValue()));
          list.add(properties.get(key).getValue());
          properties.put(key, UserProfilePropertyValue.valueOf(list));
        }
      }
      else {
        throw new BadRequestException("Invalid property "+entry.getKey());
      }
    }
    userProfileDao.put(toUserProfile(commandRequest.getUserId(), properties));
    return Optional.of(userProfile);
  }

  private UserProfile toUserProfile(
      UserId userId, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    return new UserProfile(userId, Instant.now(), properties);
  }
}
