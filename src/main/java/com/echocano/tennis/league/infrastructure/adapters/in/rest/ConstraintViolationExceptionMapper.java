package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.ErrorResponse;

import jakarta.annotation.Priority;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Response.Status status = Response.Status.BAD_REQUEST;
        String message = "Validation error in the submitted fields";
        List<String> details = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String propiedad = violation.getPropertyPath().toString();
                    if (propiedad.contains(".")) {
                        propiedad = propiedad.substring(propiedad.lastIndexOf(".") + 1);
                    }
                    return String.format("Field '%s': %s", propiedad, violation.getMessage());
                })
                .collect(Collectors.toList());
        ErrorResponse errorPayload = new ErrorResponse(message, status.getStatusCode(), details);

        return Response.status(status)
                .entity(errorPayload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}