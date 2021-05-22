package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface IStopwatch {

    @NotNull IStopwatch start();

    @NotNull IStopwatch stop();

    @NotNull IStopwatch reset();

    boolean isRunning();

    long elapsed(@NotNull TimeUnit timeUnit);

    default long elapsedMillis() {
        return elapsed(TimeUnit.MILLISECONDS);
    }

    default long elapsedNanos() {
        return elapsed(TimeUnit.NANOSECONDS);
    }

}
