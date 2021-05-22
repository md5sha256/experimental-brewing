package com.github.md5sha256.spigotutils.sql;

import org.jetbrains.annotations.NotNull;

public interface NumberQuery {

    @NotNull String generateConstraints();

    boolean test(@NotNull Number number);

    @NotNull NumberQuery negate();

}
