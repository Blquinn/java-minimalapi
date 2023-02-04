package com.github.blquinn.minimalapi.common.modules.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.github.blquinn.minimalapi.common.jpa.EntityNotFoundException;
import io.jooby.Context;
import io.jooby.MediaType;
import io.jooby.StatusCode;
import io.jooby.exception.NotFoundException;
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
        log.warn("Got status code exception.", e);
        ctx.setResponseCode(e.getStatusCode());

        ValidationErrorResponseDto res;
        if (e instanceof NotFoundException nfe) {
          var message = String.format("The path '%s' was not found.", nfe.getRequestPath());
          res = new ValidationErrorResponseDto(message, List.of());
        } else {
          res = new ValidationErrorResponseDto(cause.getLocalizedMessage(), List.of());
        }
        ctx.send(objectMapper.writeValueAsString(res));
      } else if (cause instanceof MismatchedInputException e) {
        log.warn("Request was invalid: {}", cause.getMessage());
        ctx.setResponseCode(StatusCode.BAD_REQUEST);
        var res = new ValidationErrorResponseDto(
            "Request was invalid", List.of(
            new ValidationErrorDto(".", "Failed to deserialize request body.")
        ));
        ctx.send(objectMapper.writeValueAsString(res));
      } else if (cause instanceof ValueInstantiationException e) {
        log.warn("Request was invalid: {}", cause.getMessage());
        ctx.setResponseCode(StatusCode.BAD_REQUEST);
        var res = new ValidationErrorResponseDto(
            "Request was invalid", List.of(
            new ValidationErrorDto(".", "Failed to deserialize request body.")));
        ctx.send(objectMapper.writeValueAsString(res));
      } else if (cause instanceof EntityNotFoundException e) {
        log.warn("Entity not found: {}", cause.getMessage());
        ctx.setResponseCode(StatusCode.NOT_FOUND);
        var res = new ValidationErrorResponseDto(e.getMessage(), List.of());
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
        if (!ctx.isResponseStarted()) {
            ctx.send("An unexpected error occurred.");
        }
    }
  }
}
