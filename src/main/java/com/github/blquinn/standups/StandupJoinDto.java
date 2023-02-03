package com.github.blquinn.standups;

import javax.validation.constraints.Min;

public record StandupJoinDto(@Min(1L) long userId) {
}
