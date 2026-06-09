package com.echocano.tennis.league.infrastructure.config;

import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JacksonExceptionMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Mapping error in: " + exception.getPathReference() + " -> " + exception.getMessage())
                .build();
    }
}
