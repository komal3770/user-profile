package com.spotlight.platform.userprofile.api.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.CommandEnum;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import java.time.Instant;
import java.util.Map;

public class UserCommandRequest {

  @JsonProperty
  UserId userId;
  @JsonProperty
  CommandEnum type;
  @JsonProperty Map<UserProfilePropertyName, UserProfilePropertyValue> properties;

  public UserCommandRequest(){}
  public UserCommandRequest(UserId userId, CommandEnum type, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    this.userId = userId;
    this.type = type;
    this.properties = properties;
  }

  public UserId getUserId() {
    return userId;
  }

  public CommandEnum getType() {
    return type;
  }

  public Map<UserProfilePropertyName, UserProfilePropertyValue> getProperties() {
    return properties;
  }

}
