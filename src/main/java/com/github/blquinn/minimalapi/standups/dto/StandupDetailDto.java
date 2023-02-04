package com.github.blquinn.minimalapi.standups.dto;

import com.github.blquinn.minimalapi.users.dto.UserDto;
import java.util.Collection;

public record StandupDetailDto(long id, String name, Collection<UserDto> users) {
}
