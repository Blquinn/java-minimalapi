package com.github.blquinn.common.pagination;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public record Page<T>(@Nullable Integer nextPage, @Nonnull Collection<T> contents) { }
