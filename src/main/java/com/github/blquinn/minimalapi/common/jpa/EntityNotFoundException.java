package com.github.blquinn.minimalapi.common.jpa;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String message) {
    super(message);
  }
}
