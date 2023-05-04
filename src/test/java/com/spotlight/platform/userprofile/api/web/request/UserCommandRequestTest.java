package com.spotlight.platform.userprofile.api.web.request;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import org.junit.jupiter.api.Test;

class UserCommandRequestTest {
  @Test
  void serialization_WorksAsExpected() {
    assertThatJson(UserProfileFixtures.USER_COMMAND_REQUEST)
        .isEqualTo(UserProfileFixtures.SERIALIZED_USER_COMMAND_REQUEST);
  }
}
