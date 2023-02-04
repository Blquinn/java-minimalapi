package com.github.blquinn.minimalapi.standups.dto;

import org.hibernate.validator.constraints.Length;

public record StandupCreateDto(@Length(min = 1) String name) {
}
