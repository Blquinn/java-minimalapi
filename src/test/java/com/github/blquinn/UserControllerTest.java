package com.github.blquinn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.blquinn.common.pagination.Page;
import com.github.blquinn.users.dto.UserCreateDto;
import com.github.blquinn.users.dto.UserDto;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserControllerTest extends BaseApplicationTest {

  @Test
  public void itCanCreateUsers() {
    var username = UUID.randomUUID().toString();
    var dto = client.post("/users", new UserCreateDto(username), new TypeReference<UserDto>() {});
    Assertions.assertTrue(dto.id() > 0);
    Assertions.assertEquals(dto.username(), username);
  }

  @Test
  public void itCanListUses() {
    var username = UUID.randomUUID().toString();
    var dto = client.post("/users", new UserCreateDto(username), new TypeReference<UserDto>() {});
    Assertions.assertTrue(dto.id() > 0);
    Assertions.assertEquals(dto.username(), username);
    var userPage = client.get("/users", new TypeReference<Page<UserDto>>() {});

    Assertions.assertFalse(userPage.contents().isEmpty());
  }
}
