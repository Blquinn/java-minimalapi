package com.github.blquinn.users;

import org.hibernate.validator.constraints.Length;

public record UserCreateDto(
        @Length(min = 1, max = 100)
        String username) {
}
