package com.spotlight.platform.userprofile.api.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class UserCommandRequest {

  @JsonProperty String userId;
  @JsonProperty String type;
  @JsonProperty Map<String, String> properties;

  public UserCommandRequest(String userId, String type, Map<String, String> properties) {
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

  public Map<String, String> getProperties() {
    return properties;
  }
}
