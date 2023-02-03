package com.github.blquinn.common.modules.validation;


import java.util.List;

public class ValidationException extends RuntimeException {
  private final ValidationErrorResponseDto validationErrors;

  public ValidationException(String title, ValidationErrorResponseDto validationErrors) {
    super(title);
    this.validationErrors = validationErrors;
  }

  public ValidationException(String title) {
    this(title, new ValidationErrorResponseDto(title, List.of()));
  }

  public ValidationException(ValidationErrorResponseDto validationErrors) {
    this.validationErrors = validationErrors;
  }

  @Override
  public String getMessage() {
    return validationErrors.title();
  }

  public ValidationErrorResponseDto getValidationErrors() {
    return validationErrors;
  }
}
