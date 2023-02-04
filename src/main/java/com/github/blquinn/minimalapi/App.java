package com.github.blquinn.minimalapi;

import com.github.blquinn.minimalapi.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.minimalapi.common.modules.DefaultHibernateModule;
import com.github.blquinn.minimalapi.common.modules.DefaultModule;
import com.github.blquinn.minimalapi.standups.StandupController;
import com.github.blquinn.minimalapi.standups.domain.Standup;
import com.github.blquinn.minimalapi.standups.domain.StandupUser;
import com.github.blquinn.minimalapi.users.UserController;
import com.github.blquinn.minimalapi.users.domain.User;
import io.jooby.Jooby;

public class App extends Jooby {

  // Classpath scanning for entities is broken in tests for now.
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
