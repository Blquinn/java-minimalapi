package com.github.blquinn.minimalapi.common.modules;

import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.hibernate.HibernateModule;
import io.jooby.hikari.HikariModule;
import javax.annotation.Nonnull;

public class DefaultHibernateModule implements Extension {
  private final Class<?>[] entities;

  public DefaultHibernateModule(Class<?>... entities) {
    this.entities = entities;
  }


  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    application.install(new HikariModule());
    application.install(new HibernateModule(entities));
    application.install(new JPAExtensionsModule());
  }
}
