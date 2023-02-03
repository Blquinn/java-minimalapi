package com.github.blquinn;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import com.github.blquinn.common.modules.DefaultHibernateModule;
import com.github.blquinn.common.modules.DefaultModule;
import com.github.blquinn.standups.StandupController;
import com.github.blquinn.users.UserController;
import io.jooby.Jooby;


public class Main extends Jooby {
  {
    install(new DefaultModule());
    install(new DefaultHibernateModule());

    get("/", (ctx) -> ctx.send("\uD83D\uDC4B"));
    mvc(new UserController(require(EntityManagerFactoryWrapper.class)));
    mvc(new StandupController(require(EntityManagerFactoryWrapper.class)));
  }

  public static void main(String[] args) {
    Jooby.runApp(args, Main.class);
  }
}
