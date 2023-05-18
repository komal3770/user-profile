package com.spotlight.platform.userprofile.api.core.profile;

import static com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import java.util.Optional;
import javax.ws.rs.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));

      assertThat(userProfileService.get(USER_ID))
          .usingRecursiveComparison()
          .isEqualTo(USER_PROFILE);
    }

    /** Gets for non-existing user throws exception. */
    @Test
    void getForNonExistingUser_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userProfileService.get(USER_ID))
          .isExactlyInstanceOf(EntityNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("executeCommand")
  class ExecuteCommand {

    @Test
    void executeCommandForReplaceExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThat(userProfileService.executeCommand(USER_COMMAND_REQUEST_REPLACE)).isNotNull();
    }

    @Test
    void executeCommandForReplaceInvalidProperty_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThatThrownBy(
              () -> userProfileService.executeCommand(USER_COMMAND_REQUEST_INVALID_REPLACE))
          .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void executeCommandForInvalidCommand_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThatThrownBy(
              () -> userProfileService.executeCommand(USER_COMMAND_REQUEST_INVALID_COMMAND))
          .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void executeCommandForInValidValueIncrement_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThatThrownBy(
              () -> userProfileService.executeCommand(USER_COMMAND_REQUEST_INVALID_INCREMENT))
          .isExactlyInstanceOf(NumberFormatException.class);
    }

    @Test
    void executeCommandForCollectExistingUser_returnsUser() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThat(userProfileService.executeCommand(USER_COMMAND_REQUEST_COLLECT)).isNotNull();
    }

    @Test
    void executeCommandForCollectInvalidProperties_throwsException() {
      when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      assertThatThrownBy(
              () -> userProfileService.executeCommand(USER_COMMAND_REQUEST_INVALID_COLLECT))
          .isInstanceOf(Exception.class);
    }
  }
}
