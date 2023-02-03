package com.github.blquinn.common.modules;

import com.github.blquinn.common.jpa.EntityManagerFactoryWrapper;
import io.jooby.Extension;
import io.jooby.Jooby;
import javax.annotation.Nonnull;
import javax.persistence.EntityManagerFactory;

public class JPAExtensionsModule implements Extension {
  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    application.getServices().putIfAbsent(EntityManagerFactoryWrapper.class,
        new EntityManagerFactoryWrapper(application.require(EntityManagerFactory.class)));
  }
}
