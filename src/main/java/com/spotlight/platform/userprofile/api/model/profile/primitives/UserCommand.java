package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spotlight.platform.userprofile.api.model.common.AlphaNumericalStringWithMaxLength;

public class UserCommand extends AlphaNumericalStringWithMaxLength {
  @JsonCreator
  protected UserCommand(String value) {
    super(value);
  }

  public static UserCommand valueOf(String userCommand) {
    return new UserCommand(userCommand);
  }
}
