package com.spotlight.platform.userprofile.api.web.resources;

import static com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.profile.primitives.*;
import com.spotlight.platform.userprofile.api.web.UserProfileApiApplication;

import java.util.Map;
import java.util.Optional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.vyarus.dropwizard.guice.test.ClientSupport;
import ru.vyarus.dropwizard.guice.test.jupiter.ext.TestDropwizardAppExtension;

@Execution(ExecutionMode.SAME_THREAD)
class UserCommandResourceTest {

  @RegisterExtension
  static TestDropwizardAppExtension APP =
      TestDropwizardAppExtension.forApp(UserProfileApiApplication.class)
          .randomPorts()
          .hooks(
              builder ->
                  builder.modulesOverride(
                      new AbstractModule() {
                        @Provides
                        @Singleton
                        public UserProfileDao getUserProfileDao() {
                          return mock(UserProfileDao.class);
                        }
                      }))
          .randomPorts()
          .create();

  @BeforeEach
  void beforeEach(UserProfileDao userProfileDao) {
    reset(userProfileDao);
  }

  @Nested
  @DisplayName("executeCommands")
  class ExecuteCommands {
    private static final String URL = "/apis/command";

    @Test
    void testReplaceCommand(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class)))
          .thenReturn(Optional.of(USER_PROFILE));
      Entity<?> entity =
          Entity.entity(USER_COMMAND_REQUEST, MediaType.APPLICATION_JSON_TYPE);

      try (var response = client.targetRest().path(URL).request().put(entity)) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
      }
    }

    @Test
    void testIncrementCommand(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class)))
          .thenReturn(Optional.of(USER_PROFILE));
      Entity<?> entity =
          Entity.entity(
              USER_COMMAND_REQUEST_INCREMENT, MediaType.APPLICATION_JSON_TYPE);
      try (var response = client.targetRest().path(URL).request().put(entity)) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
      }
    }

    @Test
    void testCollectCommand(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class)))
          .thenReturn(Optional.of(USER_PROFILE));
      Entity<?> entity =
          Entity.entity(
              USER_COMMAND_REQUEST_COLLECT, MediaType.APPLICATION_JSON_TYPE);
      var response = client.targetRest().path(URL).request().put(entity);
      assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    @Test
    void testReplaceCommand_NonExistingUser(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class)))
              .thenReturn(Optional.empty());
      Entity<?> entity =
              Entity.entity(USER_COMMAND_REQUEST, MediaType.APPLICATION_JSON_TYPE);

      try (var response = client.targetRest().path(URL).request().put(entity)) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
      }
    }

    @Test
    void testReplaceCommand_ServerError(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class)))
              .thenReturn(Optional.empty());
      Entity<?> entity =
              Entity.entity(USER_COMMAND_REQUEST, MediaType.APPLICATION_JSON_TYPE);

      try (var response = client.targetRest().path(URL).request().put(entity)) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
      }
    }

    @Test
    void testInvalidCommand_returnsUser(ClientSupport client, UserProfileDao userProfileDao) {
      when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(USER_PROFILE));
      UserCommandRequest request =
          new UserCommandRequest(
              USER_ID,
              CommandEnum.getCommand("INVALID"),
              Map.of(
                  UserProfilePropertyName.valueOf("property1"),
                  UserProfilePropertyValue.valueOf("property1Value")));
      Entity<?> entity = Entity.entity(request, MediaType.APPLICATION_JSON_TYPE);
      try (var response = client.targetRest().path(URL).request().put(entity)) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
      }
    }
  }
}
