package com.spotlight.platform.userprofile.api.model.profile.primitives;

import java.io.Serializable;

public enum CommandEnum implements Serializable {
  REPLACE,
  INCREMENT,
  COLLECT;

  public static CommandEnum getCommand(String command) {
    try {
      return CommandEnum.valueOf(command.toUpperCase());
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    }
  }
}
