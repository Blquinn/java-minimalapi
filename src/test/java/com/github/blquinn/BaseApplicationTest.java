package com.github.blquinn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jooby.ExecutionMode;
import io.jooby.Jooby;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApplicationTest {
  Jooby app;
  RestClient client;
  ObjectMapper objectMapper;

  @BeforeAll
  public void beforeAll() {
    app = Jooby.createApp(new String[]{"application.env=test"}, ExecutionMode.DEFAULT, App.class);
    objectMapper = app.require(ObjectMapper.class);
    app.start();
    var serverOptions = app.getServerOptions();
    var baseUrl = String.format("http://%s:%d", serverOptions.getHost(), serverOptions.getPort());
    client = new RestClient(baseUrl, objectMapper);
  }

  @AfterAll
  public void afterAll() {
    app.stop();
  }

  // TODO: Clear database before each.
}
