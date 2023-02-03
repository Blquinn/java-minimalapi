package com.github.blquinn.users;

import static com.github.blquinn.users.QUser.user;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.common.pagination.Page;
import com.github.blquinn.common.pagination.PageRequest;
import com.github.blquinn.users.domain.User;
import com.github.blquinn.users.dto.UserCreateDto;
import com.github.blquinn.users.dto.UserDto;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import io.jooby.annotations.QueryParam;
import lombok.RequiredArgsConstructor;

@Path("/users")
@RequiredArgsConstructor
public class UserController {

  private final EntityManagerFactoryWrapper emf;

  @GET
  public Page<UserDto> list(@QueryParam PageRequest pageRequest) {
    return emf.withHandle(h ->
        h.pageQuery(pageRequest, q -> q.select(user).from(user), User::toDto));
  }

  @POST
  public UserDto create(UserCreateDto dto) {
    return emf.withHandle(h ->
        h.withTransaction(t ->
            h.manager().merge(new User(dto.username())).toDto()));
  }
}
