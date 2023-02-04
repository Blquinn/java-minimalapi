package com.github.blquinn.minimalapi.common.modules.validation;

import java.util.Collection;

public record ValidationErrorResponseDto(String title, Collection<ValidationErrorDto> errors) {
}
