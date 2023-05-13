package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        Map<String, Object> resp = new HashMap<>();
        if(exception instanceof BadRequestException){
            resp.put("message", "Bad request");
            resp.put("error", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
        resp.put("message", "Something went wrong");
        return Response.serverError().build();
    }
}
