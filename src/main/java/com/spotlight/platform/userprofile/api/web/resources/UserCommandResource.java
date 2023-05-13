package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@Path("/apis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserCommandResource {

  private final UserProfileService userProfileService;

  @Inject
  public UserCommandResource(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @PUT
  @Path("/command")
  public Response executeCommand(UserCommandRequest request) {
    Optional<UserProfile> profile = userProfileService.executeCommand(request);
    if (profile.isPresent()) return Response.ok(profile.get()).build();
    return Response.status(Response.Status.FORBIDDEN)
        .entity(Map.of("message", "Process terminated"))
        .build();
  }
}
