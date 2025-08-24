package com.example.mapper;

import jakarta.ws.rs.ext.*;
import jakarta.ws.rs.core.*;

import java.util.Map;

import jakarta.ws.rs.WebApplicationException;

@Provider
public class ErrorMapper implements ExceptionMapper<Throwable> {
    public Response toResponse(Throwable ex) {
        int status = (ex instanceof WebApplicationException wae)
                ? wae.getResponse().getStatus()
                : 500;

        var body = Map.of(
                "error", ex.getClass().getSimpleName(),
                "message", ex.getMessage(),
                "status", status);

        return Response
                .status(status)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
