package com.spotlight.platform.userprofile.api.web;

import com.spotlight.platform.userprofile.api.core.json.JsonMapper;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.configuration.UserProfileApiConfiguration;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.exceptionmappers.EntityNotFoundExceptionMapper;
import com.spotlight.platform.userprofile.api.web.healthchecks.PreventStartupWarningHealthCheck;
import com.spotlight.platform.userprofile.api.web.modules.UserProfileApiModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class UserProfileApiApplication extends Application<UserProfileApiConfiguration> {

  private GuiceBundle guiceBundle;

  @Override
  public String getName() {
    return UserProfileApiConfiguration.APPLICATION_NAME;
  }

  @Override
  public void initialize(Bootstrap<UserProfileApiConfiguration> bootstrap) {
    super.initialize(bootstrap);
    guiceBundle =
        GuiceBundle.builder()
            .enableAutoConfig(getClass().getPackage().getName())
            .modules(new UserProfileApiModule())
            .printDiagnosticInfo()
            .build();
    bootstrap.addBundle(guiceBundle);
    bootstrap.setObjectMapper(JsonMapper.getInstance());
  }

  @Override
  public void run(UserProfileApiConfiguration configuration, Environment environment) {
    registerHealthChecks(environment);
    registerExceptionMappers(environment);
    loadUserDetails();
  }

  public static void main(String[] args) throws Exception {
    new UserProfileApiApplication().run(args);
  }

  private void registerHealthChecks(Environment environment) {
    environment
        .healthChecks()
        .register(
            PreventStartupWarningHealthCheck.NAME,
            getInstance(PreventStartupWarningHealthCheck.class));
  }

  private void registerExceptionMappers(Environment environment) {
    environment.jersey().register(getInstance(EntityNotFoundExceptionMapper.class));
  }

  private void loadUserDetails(){
    UserProfileDao dao = getInstance(UserProfileDao.class);
    Map<UserProfilePropertyName, UserProfilePropertyValue> prop1 = new HashMap<>();
    prop1.put(UserProfilePropertyName.valueOf("prop1"),UserProfilePropertyValue.valueOf(100));
    prop1.put(UserProfilePropertyName.valueOf("prop2"),UserProfilePropertyValue.valueOf("value2"));
    dao.put(new UserProfile(UserId.valueOf("1"), Instant.now(), prop1));
    Map<UserProfilePropertyName, UserProfilePropertyValue> prop2 = new HashMap<>();
    prop2.put(UserProfilePropertyName.valueOf("prop1"),UserProfilePropertyValue.valueOf(400));
    prop2.put(UserProfilePropertyName.valueOf("prop2"),UserProfilePropertyValue.valueOf("value3"));
    dao.put(new UserProfile(UserId.valueOf("2"), Instant.now(), prop2));
    dao.put(new UserProfile(UserId.valueOf("3"), Instant.now(), new HashMap<>()));
  }

  private <T> T getInstance(Class<T> clazz) {
    return guiceBundle.getInjector().getInstance(clazz);
  }
}
