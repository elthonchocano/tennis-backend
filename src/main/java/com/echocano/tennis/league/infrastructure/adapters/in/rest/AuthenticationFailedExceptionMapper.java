package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import org.jboss.logging.Logger;

import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.ErrorResponse;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthenticationFailedExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {

    private static final Logger LOG = Logger.getLogger(AuthenticationFailedExceptionMapper.class);

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        LOG.warn("Authentication failure (401) intercepted: " + exception.getMessage());
        Response.Status status = Response.Status.UNAUTHORIZED;
        String message = "Invalid, expired, or missing authentication token.";

        ErrorResponse errorPayload = new ErrorResponse(message, status.getStatusCode());

        return Response.status(status)
                .entity(errorPayload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
