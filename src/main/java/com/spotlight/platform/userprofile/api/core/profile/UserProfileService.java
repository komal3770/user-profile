package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

/** User service for business logic. */
public class UserProfileService {
  private final UserProfileDao userProfileDao;

  public static final String REGEX_USER_PROP_VALUES = "[\\[\\]\\s]";

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
    // Get Existing
    UserProfile userProfile = get(commandRequest.getUserId());
    Map<UserProfilePropertyName, UserProfilePropertyValue> existingProperties =
        new HashMap<>(userProfile.userProfileProperties());
    // Check if properties present in userProfile
    if(commandRequest.getType() == null){
      throw new BadRequestException("Invalid command type");
    }
    if (commandRequest.getProperties().keySet().stream()
        .anyMatch(key -> !existingProperties.containsKey(key))) {
      throw new BadRequestException("Invalid property present which is not there userprofile");
    }

    switch (commandRequest.getType()) {
      case REPLACE ->
        existingProperties.putAll(commandRequest.getProperties());

      case COLLECT ->
        collectProperties(existingProperties, commandRequest.getProperties());

      case INCREMENT ->
        incrementProperties(existingProperties, commandRequest.getProperties());
      default -> throw new BadRequestException("Invalid command");
    }

    userProfile = toUserProfile(commandRequest.getUserId(), existingProperties);
    userProfileDao.put(userProfile);
    return Optional.of(userProfile);
  }

  public List<UserProfile> executeCommands(List<UserCommandRequest> commandRequests){
    List<UserProfile> userProfiles = new ArrayList<>();
    for(UserCommandRequest commandRequest : commandRequests){
      Optional<UserProfile> profile = executeCommand(commandRequest);
      if(profile.isPresent()){
        userProfiles.add(profile.get());
      }
    }
    return userProfiles;
  }

  /** Internal method for create UserProfile & which will be persisted in storage. */
  private UserProfile toUserProfile(
      UserId userId, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    return new UserProfile(userId, Instant.now(), properties);
  }

  /**
   * Adds values to a list of the values in the user profile property.
   *
   * @param existingProperties the existing properties
   * @param requestProperties the request properties
   */
  void collectProperties(
      Map<UserProfilePropertyName, UserProfilePropertyValue> existingProperties,
      Map<UserProfilePropertyName, UserProfilePropertyValue> requestProperties) {
    requestProperties
        .forEach((key, value) -> {
          String[] stringArr =
              value.getValue().toString().replaceAll(REGEX_USER_PROP_VALUES, "").split(",");
          String[] existingStringArr =
              existingProperties
                  .get(key)
                  .getValue()
                  .toString()
                  .replaceAll(REGEX_USER_PROP_VALUES, "")
                  .split(",");

          existingProperties.put(
              key,
              UserProfilePropertyValue.valueOf(
                  Stream.concat(Arrays.stream(stringArr), Arrays.stream(existingStringArr))
                      .collect(Collectors.toList())));
        });
  }

  /**
   * Increments the current value in the profile.
   *
   * @param existingProperties the existing propeNrties
   * @param requestProperties the request properties
   */
  void incrementProperties(
      Map<UserProfilePropertyName, UserProfilePropertyValue> existingProperties,
      Map<UserProfilePropertyName, UserProfilePropertyValue> requestProperties) {
    requestProperties
        .forEach((key, value) -> {
          int val = Integer.parseInt(value.getValue().toString());
          val = Integer.parseInt(existingProperties.get(key).getValue().toString()) + val;
          existingProperties.put(key, UserProfilePropertyValue.valueOf(val));
        });
  }
}
