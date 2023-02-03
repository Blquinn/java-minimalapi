package com.github.blquinn.common.pagination;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public record Page<T>(@Nullable Integer nextPage, @Nonnull Collection<T> contents) {
  public <O> Page<O> map(Function<T, O> mapper) {
    return new Page<>(nextPage, contents.stream().map(mapper).collect(Collectors.toList()));
  }
}
