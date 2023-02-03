package com.github.blquinn.common.modules;

import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.hibernate.HibernateModule;
import io.jooby.hikari.HikariModule;
import javax.annotation.Nonnull;

public class DefaultHibernateModule implements Extension {
  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    application.install(new HikariModule());
    application.install(new HibernateModule());
    application.install(new JPAExtensionsModule());
  }
}
