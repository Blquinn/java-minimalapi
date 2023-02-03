package com.github.blquinn.common.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blquinn.common.modules.validation.ExceptionHandler;
import io.jooby.AccessLogHandler;
import io.jooby.Context;
import io.jooby.Extension;
import io.jooby.Jooby;
import javax.annotation.Nonnull;

public class DefaultModule implements Extension {

  private static final String thumbsUp = "\uD83D\uDC4D";

  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    var jacksonModule = new DefaultJacksonModule();
    application.install(jacksonModule);
    application.install(new ValidationModule(jacksonModule.getJacksonModule()));
    application.getRouter().get("/health", this::healthCheck);
    application.decorator(new AccessLogHandler());
    application.error(new ExceptionHandler(application.require(ObjectMapper.class))::handle);
  }

  private Object healthCheck(Context ctx) {
    ctx.setResponseHeader("Content-Type", "text/plain; charset=utf-8");
    return ctx.send(thumbsUp);
  }
}
