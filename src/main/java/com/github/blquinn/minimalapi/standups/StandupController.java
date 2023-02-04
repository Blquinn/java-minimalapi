package com.github.blquinn.minimalapi.standups;

import static com.github.blquinn.minimalapi.standups.QStandup.standup;
import static com.github.blquinn.minimalapi.standups.QStandupUser.standupUser;
import static com.github.blquinn.minimalapi.users.QUser.user;

import com.github.blquinn.minimalapi.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.minimalapi.common.jpa.Preconditions;
import com.github.blquinn.minimalapi.common.modules.validation.ValidationException;
import com.github.blquinn.minimalapi.common.pagination.Page;
import com.github.blquinn.minimalapi.common.pagination.PageRequest;
import com.github.blquinn.minimalapi.standups.domain.Standup;
import com.github.blquinn.minimalapi.standups.domain.StandupUser;
import com.github.blquinn.minimalapi.standups.dto.StandupCreateDto;
import com.github.blquinn.minimalapi.standups.dto.StandupDetailDto;
import com.github.blquinn.minimalapi.standups.dto.StandupDto;
import com.github.blquinn.minimalapi.standups.dto.StandupJoinDto;
import com.github.blquinn.minimalapi.users.domain.User;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import io.jooby.annotations.PathParam;
import io.jooby.annotations.QueryParam;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

@Path("/standups")
@RequiredArgsConstructor
public class StandupController {
  @Nonnull
  private final EntityManagerFactoryWrapper emf;

  @GET
  public Page<StandupDto> list(@QueryParam PageRequest pageRequest) {
    return emf.withHandle(
        h -> h.pageQuery(pageRequest, q -> q.select(standup).from(standup), Standup::toDto));
  }

  @GET("/{id}")
  public StandupDetailDto getById(@PathParam long id) {
    return emf.withHandle(h -> Preconditions.checkEntityNotNull(
        h.query().select(standup).from(standup).where(standup.id.eq(id))
            .leftJoin(standup.standupUsers, standupUser).fetchJoin()
            .leftJoin(standupUser.user, user).fetchJoin().fetchOne(), "standup").toDetailDto());
  }

  @POST
  public StandupDto create(StandupCreateDto dto) {
    return emf.withHandle(
        h -> h.withTransaction(t -> h.manager().merge(new Standup(dto.name()))).toDto());
  }

  @POST("/{id}/join")
  public void join(@PathParam long id, StandupJoinDto joinDto) {
    emf.withHandle(h -> h.withTransaction(t -> {
      var standup =
          Preconditions.checkEntityNotNull(h.manager().find(Standup.class, id), "standup");

      var user =
          Preconditions.checkEntityNotNull(h.manager().find(User.class, joinDto.userId()), "user");

      var exists = h.query().select(standupUser.count().gt(0)).from(standupUser)
          .where(standupUser.standup.eq(standup), standupUser.user.eq(user)).fetchOne();

      if (exists) {
        throw new ValidationException("StandupUser already exists.");
      }

      return h.manager().merge(new StandupUser(standup, user));
    }));
  }
}
