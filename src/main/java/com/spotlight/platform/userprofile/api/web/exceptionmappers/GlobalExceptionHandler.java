package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class GlobalExceptionHandler implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException exception) {
    exception.printStackTrace();
    Map<String, Object> resp = new HashMap<>();
    if (exception instanceof BadRequestException) {
      resp.put("message", "Bad request");
      resp.put("error", exception.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
    }
    resp.put("message", "Something went wrong");
    return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp).build();
  }
}
