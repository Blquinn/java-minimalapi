package com.github.blquinn.common.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.json.JacksonModule;
import javax.annotation.Nonnull;

public class DefaultJacksonModule implements Extension {
  private JacksonModule jacksonModule;

  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    var objectMapper = new ObjectMapper().findAndRegisterModules();
    application.getServices().putIfAbsent(ObjectMapper.class, objectMapper);
    jacksonModule = new JacksonModule(objectMapper);
    application.install(jacksonModule);
  }

  public JacksonModule getJacksonModule() {
    return jacksonModule;
  }
}
