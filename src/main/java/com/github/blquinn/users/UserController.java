package com.github.blquinn.users;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.common.pagination.Page;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import lombok.RequiredArgsConstructor;

import static com.github.blquinn.users.QUser.user;

@Path("/users")
@RequiredArgsConstructor
public class UserController {

    private final EntityManagerFactoryWrapper emf;

    @GET
    public Page<UserDto> list() {
        var userDtos = emf.withHandle(h -> h.query()
                .select(user)
                .from(user)
                .stream()
                .map(User::toDto)
                .toList());

        return new Page<>(null, userDtos);
    }

    @POST
    public UserDto create(UserCreateDto dto) {
        return emf.withHandle(h ->
                h.withTransaction(t ->
                        h.manager().merge(new User(dto.username())).toDto()));
    }
}
