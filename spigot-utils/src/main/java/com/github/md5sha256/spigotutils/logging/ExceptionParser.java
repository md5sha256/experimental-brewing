package com.github.md5sha256.spigotutils.logging;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ExceptionParser {

    @NotNull LogDump parse(@NotNull Throwable throwable, @NotNull LogDump logger);

}
