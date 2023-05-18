package com.spotlight.platform.userprofile.api.model.profile.primitives;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CommandEnumTest {

  @Test
  void getCommandValidValue_returnsCommandEnum() {
    assertThat(CommandEnum.getCommand("REPLACE")).isEqualTo(CommandEnum.REPLACE);
  }

  @Test
  void getCommandInvalidValue_returnsCommandEnum() {
    assertThat(CommandEnum.getCommand("INVALID")).isNull();
  }
}
