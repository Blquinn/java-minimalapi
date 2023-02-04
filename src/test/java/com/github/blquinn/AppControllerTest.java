package com.github.blquinn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jooby.MockRouter;
import org.junit.jupiter.api.Test;

public class AppControllerTest {
  @Test
  public void itWavesHello() {
    MockRouter router = new MockRouter(new App());
    assertEquals("👋", router.get("/").value());
  }
}
