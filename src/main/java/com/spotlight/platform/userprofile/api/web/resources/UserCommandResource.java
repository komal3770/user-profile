package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserCommand;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/commands")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserCommandResource {

  private final UserProfileService userProfileService;

  @Inject
  public UserCommandResource(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @Path("{userCommand}")
  @PUT
  public Response executeCommand(@Valid @PathParam("userCommand") UserCommand userCommand) {
    return Response.ok().build();
  }
}
