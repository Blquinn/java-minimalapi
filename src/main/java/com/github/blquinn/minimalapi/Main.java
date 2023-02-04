package com.github.blquinn.minimalapi;

import io.jooby.Jooby;


public class Main {

  public static void main(String[] args) {
    Jooby.runApp(args, App.class);
  }
}
