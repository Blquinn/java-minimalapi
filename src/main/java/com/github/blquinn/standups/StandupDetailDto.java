package com.github.blquinn.standups;

import com.github.blquinn.users.UserDto;

import java.util.Collection;

public record StandupDetailDto(long id, String name, Collection<UserDto> users) {
}
