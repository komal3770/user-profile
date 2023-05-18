package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.time.Instant;
import java.util.Arrays;
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
              UserProfilePropertyValue.valueOf("100"),
              UserProfilePropertyName.valueOf("collectProperty"),
              UserProfilePropertyValue.valueOf(Arrays.asList("abc", "xyz"))));

  public static final String SERIALIZED_USER_PROFILE =
      FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");

  public static final UserCommandRequest USER_COMMAND_REQUEST_REPLACE =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.REPLACE,
          Map.of(
              UserProfilePropertyName.valueOf("property1"),
              UserProfilePropertyValue.valueOf("property1Value")));

  public static final UserCommandRequest USER_COMMAND_REQUEST_INVALID_REPLACE =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.REPLACE,
          Map.of(
              UserProfilePropertyName.valueOf("invalidProperty"),
              UserProfilePropertyValue.valueOf("invalidPropertyValue")));

  public static final String SERIALIZED_USER_COMMAND_REQUEST =
      FixtureHelpers.fixture("/fixtures/web/request/userCommandRequest.json");

  public static final UserCommandRequest USER_COMMAND_REQUEST_INCREMENT =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.INCREMENT,
          Map.of(
              UserProfilePropertyName.valueOf("intProperty"),
              UserProfilePropertyValue.valueOf("10")));

  public static final UserCommandRequest USER_COMMAND_REQUEST_INVALID_INCREMENT =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.INCREMENT,
          Map.of(
              UserProfilePropertyName.valueOf("intProperty"),
              UserProfilePropertyValue.valueOf("stringValue")));

  public static final UserCommandRequest USER_COMMAND_REQUEST_COLLECT =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.COLLECT,
          Map.of(
              UserProfilePropertyName.valueOf("collectProperty"),
              UserProfilePropertyValue.valueOf(Arrays.asList("abc", "xyz"))));

  public static final UserCommandRequest USER_COMMAND_REQUEST_INVALID_COLLECT =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.COLLECT,
          Map.of(
              UserProfilePropertyName.valueOf("collectProperty"),
              UserProfilePropertyValue.valueOf(null)));

  public static final UserCommandRequest USER_COMMAND_REQUEST_INVALID_COMMAND =
      new UserCommandRequest(
          USER_ID,
          CommandEnum.REPLACE,
          Map.of(
              UserProfilePropertyName.valueOf("invalidProperty"),
              UserProfilePropertyValue.valueOf("invalidPropertyValue")));
}
