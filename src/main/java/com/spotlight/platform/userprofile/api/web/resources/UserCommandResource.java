package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.web.request.UserCommandRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * APIs for commands to be called on UserProfile.
 *
 * @author Komal.patil
 */
@Path("/commands")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserCommandResource {

  private final UserProfileService userProfileService;

  @Inject
  public UserCommandResource(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  /**
   * This api accepts userId, command & properties. Executed the requested command and returns
   * response.
   *
   * @param request the request
   * @return the response
   */
  @PUT
  @Path("/execute")
  public Response executeCommand(UserCommandRequest request) {
    Optional<UserProfile> profile = userProfileService.executeCommand(request);
    if (profile.isPresent()) {
      return Response.ok(profile.get()).build();
    }
    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(Map.of("message", "Unable to process request"))
        .build();
  }

  @PUT
  @Path("/execute-bulk")
  public Response executeCommandBulk(List<UserCommandRequest> requests) {
    List<UserProfile> profiles = userProfileService.executeCommands(requests);
    if (profiles != null) {
      return Response.ok(profiles).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(Map.of("message", "Unable to process request"))
        .build();
  }
}
