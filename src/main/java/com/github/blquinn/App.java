package com.github.blquinn;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.common.modules.DefaultHibernateModule;
import com.github.blquinn.common.modules.DefaultModule;
import com.github.blquinn.standups.StandupController;
import com.github.blquinn.standups.domain.Standup;
import com.github.blquinn.standups.domain.StandupUser;
import com.github.blquinn.users.UserController;
import com.github.blquinn.users.domain.User;
import io.jooby.Jooby;

public class App extends Jooby {

  public final static Class<?>[] ENTITY_CLASSES = {User.class, Standup.class, StandupUser.class};

  private final static String handWavingEmoji = "👋";

  {
    install(new DefaultModule());
    install(new DefaultHibernateModule(ENTITY_CLASSES));

    get("/", (ctx) -> ctx.send(handWavingEmoji));
    mvc(new UserController(require(EntityManagerFactoryWrapper.class)));
    mvc(new StandupController(require(EntityManagerFactoryWrapper.class)));
  }
}
