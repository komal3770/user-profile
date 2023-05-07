package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.time.Instant;
import java.util.Map;

public class UserProfileFixtures {
  public static final UserId USER_ID = UserId.valueOf("existing-user-id");
  public static final UserId NON_EXISTING_USER_ID = UserId.valueOf("non-existing-user-id");
  public static final UserId INVALID_USER_ID = UserId.valueOf("invalid-user-id-%");

  public static final Instant LAST_UPDATE_TIMESTAMP = Instant.parse("2021-06-01T09:16:36.123Z");

  public static final UserProfile USER_PROFILE =
      new UserProfile(
          USER_ID,
          LAST_UPDATE_TIMESTAMP,
          Map.of(
              UserProfilePropertyName.valueOf("property1"),
              UserProfilePropertyValue.valueOf("property1Value"),
              UserProfilePropertyName.valueOf("intProperty"),
              UserProfilePropertyValue.valueOf("100")));

  public static final String SERIALIZED_USER_PROFILE =
      FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");

  public static final UserCommandRequest USER_COMMAND_REQUEST =
      new UserCommandRequest(
          "existing-user-id",
          "replace",
          Map.of(
              UserProfilePropertyName.valueOf("property1"),
              UserProfilePropertyValue.valueOf("property1Value")));

  public static final String SERIALIZED_USER_COMMAND_REQUEST =
      FixtureHelpers.fixture("/fixtures/web/request/userCommandRequest.json");

  public static final UserCommandRequest USER_COMMAND_REQUEST_INCREMENT =
      new UserCommandRequest("existing-user-id", "replace", Map.of(UserProfilePropertyName.valueOf("intProperty"),
          UserProfilePropertyValue.valueOf("10")));
}
