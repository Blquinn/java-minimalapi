package com.github.blquinn.minimalapi.standups.dto;

import javax.validation.constraints.Min;

public record StandupJoinDto(@Min(1L) long userId) {
}
