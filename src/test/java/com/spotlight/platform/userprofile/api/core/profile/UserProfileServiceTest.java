package com.spotlight.platform.userprofile.api.core.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
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

class UserProfileServiceTest {
  private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
  private final UserProfileService userProfileService = new UserProfileService(userProfileDaoMock);

  @Nested
  @DisplayName("get")
  class Get {
    @Test
    void getForExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

      assertThat(userProfileService.get(UserProfileFixtures.USER_ID))
          .usingRecursiveComparison()
          .isEqualTo(UserProfileFixtures.USER_PROFILE);
    }

    @Test
    void getForNonExistingUser_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userProfileService.get(UserProfileFixtures.USER_ID))
          .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void putForNonExistingUser_returnsUser() {
      UserProfile userProfile =
          new UserProfile(UserId.valueOf("new-user-id"), Instant.now(), new HashMap<>());

      assertThat(userProfileService.put(userProfile)).isEqualTo(true);
    }

    @Test
    void putForExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
      UserProfile userProfile =
          new UserProfile(UserProfileFixtures.USER_ID, Instant.now(), new HashMap<>());
      assertThat(userProfileService.put(userProfile)).isEqualTo(false);
    }

    @Test
    void executeCommandForExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
      UserCommandRequest request =
          new UserCommandRequest(
              "existing-user-id",
              "replace",
              Map.of(
                  UserProfilePropertyName.valueOf("property1"),
                  UserProfilePropertyValue.valueOf("property1Value")));
      assertThat(userProfileService.executeCommand(request)).isEqualTo(true);
    }

    @Test
    void executeCommandForNonExistingProperty_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class)))
          .thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
      UserCommandRequest request =
          new UserCommandRequest(
              "existing-user-id",
              "replace",
              Map.of(
                  UserProfilePropertyName.valueOf("invalidProperty"),
                  UserProfilePropertyValue.valueOf("invalidPropertyValue")));
      assertThat(userProfileService.executeCommand(request)).isEqualTo(true);
    }
  }
}
