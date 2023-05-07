package com.spotlight.platform.userprofile.api.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import java.util.Map;

public class UserCommandRequest {

  @JsonProperty String userId;
  @JsonProperty String type;
  @JsonProperty Map<UserProfilePropertyName, UserProfilePropertyValue> properties;

  public UserCommandRequest(){}
  public UserCommandRequest(String userId, String type, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
    this.userId = userId;
    this.type = type;
    this.properties = properties;
  }

  public String getUserId() {
    return userId;
  }

  public String getType() {
    return type;
  }

  public Map<UserProfilePropertyName, UserProfilePropertyValue> getProperties() {
    return properties;
  }
}
