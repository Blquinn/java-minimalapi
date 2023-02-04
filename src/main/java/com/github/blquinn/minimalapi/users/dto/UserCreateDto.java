package com.github.blquinn.minimalapi.users.dto;

import org.hibernate.validator.constraints.Length;

public record UserCreateDto(
    @Length(min = 1, max = 100)
    String username) {
}
