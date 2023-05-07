package com.spotlight.platform.userprofile.api.web.request;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UserCommandRequestTest {
  @Test
  void serialization_WorksAsExpected() {
    assertThatJson(UserProfileFixtures.USER_COMMAND_REQUEST)
        .isEqualTo(UserProfileFixtures.SERIALIZED_USER_COMMAND_REQUEST);
  }

  @Test
  void serializationEmptyObject() {
    UserCommandRequest request = new UserCommandRequest();
    assertEquals(request.userId, null);
    assertEquals(request.type, null);
    assertEquals(request.properties, null);
  }
}
