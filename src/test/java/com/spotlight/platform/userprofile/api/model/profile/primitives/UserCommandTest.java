package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.userprofile.api.model.common.AlphaNumericalStringWithMaxLengthAbstractTest;

public class UserCommandTest extends AlphaNumericalStringWithMaxLengthAbstractTest<UserCommand> {

  @Override
  protected UserCommand getInstance(String value) {
    return UserCommand.valueOf(value);
  }
}
