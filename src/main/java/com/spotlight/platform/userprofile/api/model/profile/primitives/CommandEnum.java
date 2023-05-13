package com.spotlight.platform.userprofile.api.model.profile.primitives;

import java.io.Serializable;

public enum CommandEnum implements Serializable {
  REPLACE("replace"), INCREMENT("increment"), COLLECT("collect");

  private final String value;

  CommandEnum(String value){
    this.value = value;
  }

  public static CommandEnum getCommand(String command){
    try{
      return CommandEnum.valueOf(command);
    }
    catch (Exception exception){
      exception.printStackTrace();
      return null;
    }
  }
}
