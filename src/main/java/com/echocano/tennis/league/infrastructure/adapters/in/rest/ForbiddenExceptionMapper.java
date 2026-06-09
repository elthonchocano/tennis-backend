package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import org.jboss.logging.Logger;

import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.ErrorResponse;

import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    private static final Logger LOG = Logger.getLogger(ForbiddenExceptionMapper.class);

    @Override
    public Response toResponse(ForbiddenException exception) {
        LOG.warn("Access denied (403) intercepted due to insufficient privileges.");
        Response.Status status = Response.Status.FORBIDDEN;
        String message = "You do not have the necessary permissions to access this resource.";
        ErrorResponse errorPayload = new ErrorResponse(message, status.getStatusCode());
        return Response.status(status)
                .entity(errorPayload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}