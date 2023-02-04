package com.github.blquinn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.blquinn.common.pagination.Page;
import com.github.blquinn.standups.dto.StandupCreateDto;
import com.github.blquinn.standups.dto.StandupDto;
import com.github.blquinn.standups.dto.StandupJoinDto;
import com.github.blquinn.users.dto.UserCreateDto;
import com.github.blquinn.users.dto.UserDto;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StandupControllerTest extends BaseApplicationTest {

  StandupDto createRandomStandup() {
    var name = UUID.randomUUID().toString();
    var res = client.post("/standups", new StandupCreateDto(name), new TypeReference<StandupDto>() {});
    Assertions.assertEquals(name, res.name());
    return res;
  }

  @Test
  public void itCanCreateStandups() {
    createRandomStandup();
  }

  @Test
  public void itCanListStandups() {
    var standup = createRandomStandup();
    var standups = client.get("/standups", new TypeReference<Page<StandupDto>>() {});
    Assertions.assertTrue(standups.contents().stream().anyMatch(s -> s.name().equals(standup.name())));
  }

  @Test
  public void usersCanJoinStandups() {
    var standup = createRandomStandup();
    var user = client.post("/users", new UserCreateDto(UUID.randomUUID().toString()), new TypeReference<UserDto>() {});
    client.post(String.format("/standups/%d/join", standup.id()), new StandupJoinDto(user.id()), null, 200);
  }
}
