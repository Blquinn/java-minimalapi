package com.github.blquinn.minimalapi.common.jpa;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Preconditions {
  @Nonnull
  public static <T> T checkEntityNotNull(@Nullable T entity, String entityName)
      throws EntityNotFoundException {
    if (entity == null) {
      throw new EntityNotFoundException(String.format("%s not found.", entityName));
    }

    return entity;
  }
}
