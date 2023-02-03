package com.github.blquinn.standups.dto;

import com.github.blquinn.users.dto.UserDto;
import java.util.Collection;

public record StandupDetailDto(long id, String name, Collection<UserDto> users) {
}
