package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.util.List;

import org.jboss.logging.Logger;

import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.ErrorResponse;

import jakarta.annotation.Priority;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = "An internal server error has occurred.";
        List<String> details = null;
        if (exception instanceof WebApplicationException webAppException) {
            int statusCode = webAppException.getResponse().getStatus();
            status = Response.Status.fromStatusCode(statusCode);
            message = exception.getMessage();
        } else if (exception instanceof IllegalArgumentException) {
            status = Response.Status.BAD_REQUEST;
            message = exception.getMessage();
        }
        LOG.error("Error intercepted: " + exception.getMessage(), exception);
        ErrorResponse errorPayload = new ErrorResponse(message, status.getStatusCode(), details);
        return Response.status(status)
                .entity(errorPayload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}