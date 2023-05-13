package com.spotlight.platform.userprofile.api.core.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.CommandEnum;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.ws.rs.BadRequestException;

/** The type User profile service test. */
class UserProfileServiceTest {
  private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
  private final UserProfileService userProfileService = new UserProfileService(userProfileDaoMock);

  /** The type Get. */
  @Nested
  @DisplayName("get")
  class Get {
    /** Gets for existing user returns user. */
    @Test
    void getForExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      assertThat(userProfileService.get(UserProfileFixtures.USER_ID))
          .usingRecursiveComparison()
          .isEqualTo(UserProfileFixtures.USER_PROFILE);
    }

    /** Gets for non existing user throws exception. */
    @Test
    void getForNonExistingUser_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userProfileService.get(UserProfileFixtures.USER_ID))
          .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void executeCommandForExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      UserCommandRequest request =
          new UserCommandRequest(
              UserProfileFixtures.USER_ID,
              CommandEnum.REPLACE,
              Map.of(
                  UserProfilePropertyName.valueOf("property1"),
                  UserProfilePropertyValue.valueOf("property1Value")));
      assertThat(userProfileService.executeCommand(request)).isNotNull();
    }

    @Test
    void executeCommandForNonExistingProperty_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      UserCommandRequest request =
          new UserCommandRequest(
              UserProfileFixtures.USER_ID,
              CommandEnum.REPLACE,
              Map.of(
                  UserProfilePropertyName.valueOf("invalidProperty"),
                  UserProfilePropertyValue.valueOf("invalidPropertyValue")));

      assertThatThrownBy(() -> userProfileService.executeCommand(request))
          .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void executeCommandForInvalidCommand_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      UserCommandRequest request =
          new UserCommandRequest(
              UserProfileFixtures.USER_ID,
              CommandEnum.getCommand("INVALID"),
              Map.of(
                  UserProfilePropertyName.valueOf("invalidProperty"),
                  UserProfilePropertyValue.valueOf("invalidPropertyValue")));

      assertThatThrownBy(() -> userProfileService.executeCommand(request))
          .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void executeCommandForValidCommand_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
              .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      UserCommandRequest request =
              new UserCommandRequest(
                      UserProfileFixtures.USER_ID,
                      CommandEnum.getCommand("REPLACE"),
                      Map.of(
                              UserProfilePropertyName.valueOf("property1"),
                              UserProfilePropertyValue.valueOf("property1Value")));

      assertThat(userProfileService.executeCommand(request)).isNotNull();
    }
  }
}
