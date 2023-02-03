package com.github.blquinn.common.modules.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.jooby.Context;
import io.jooby.MediaType;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExceptionHandler {
    private final ObjectMapper objectMapper;

    public ExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(Context ctx, Throwable cause, StatusCode statusCode) {
        ctx.setDefaultResponseType(MediaType.json);
        var log = ctx.getRouter().getLog();

        try {
            if (cause instanceof StatusCodeException e) {
                log.warn("Got status code exception: {}", cause.getMessage());
                ctx.setResponseCode(e.getStatusCode());
                var res = new ValidationErrorResponseDto(cause.getLocalizedMessage(), List.of());
                ctx.send(objectMapper.writeValueAsString(res));
            } else if (cause instanceof MismatchedInputException e) {
                log.warn("Request was invalid: {}", cause.getMessage());
                ctx.setResponseCode(StatusCode.BAD_REQUEST);
                var res = new ValidationErrorResponseDto(
                        "Request was invalid", List.of(
                        new ValidationErrorDto(".", "Failed to deserialize request body.")
                )
                );
                ctx.send(objectMapper.writeValueAsString(res));
            } else if (cause instanceof ValueInstantiationException e) {
                log.warn("Request was invalid: {}", cause.getMessage());
                ctx.setResponseCode(StatusCode.BAD_REQUEST);
                var res = new ValidationErrorResponseDto(
                        "Request was invalid", List.of(
                        new ValidationErrorDto(".", "Failed to deserialize request body.")));
                ctx.send(objectMapper.writeValueAsString(res));
            } else if (cause instanceof ValidationException e) {
                log.warn("Request was invalid: {}", cause.getMessage());
                ctx.setResponseCode(StatusCode.BAD_REQUEST);
                var res = objectMapper.writeValueAsString(e.getValidationErrors());
                ctx.send(res);
            } else {
                log.error(
                        "Error handler unexpected error: {} - {}\n{}",
                        statusCode.value(), cause.getLocalizedMessage(),
                        Arrays.stream(cause.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.joining("\n\t"))
                );

                ctx.setResponseCode(StatusCode.SERVER_ERROR);
                var res = Map.of("detail", "An unexpected error occurred.");
                ctx.send(objectMapper.writeValueAsString(res));
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object while handling exception.", e);
            if (!ctx.isResponseStarted())
                ctx.send("An unexpected error occurred.");
        }
    }
}
