package com.github.blquinn.standups;

import static com.github.blquinn.standups.QStandup.standup;
import static com.github.blquinn.standups.QStandupUser.standupUser;
import static com.github.blquinn.users.QUser.user;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.common.jpa.Preconditions;
import com.github.blquinn.common.modules.validation.ValidationException;
import com.github.blquinn.common.pagination.Page;
import com.github.blquinn.common.pagination.PageRequest;
import com.github.blquinn.standups.domain.Standup;
import com.github.blquinn.standups.domain.StandupUser;
import com.github.blquinn.standups.dto.StandupCreateDto;
import com.github.blquinn.standups.dto.StandupDetailDto;
import com.github.blquinn.standups.dto.StandupDto;
import com.github.blquinn.standups.dto.StandupJoinDto;
import com.github.blquinn.users.domain.User;
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
