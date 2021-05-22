package com.github.md5sha256.experiementalbrewing.api.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SimilarLike<T> {

    boolean isSimilar(@NotNull T other);

}
