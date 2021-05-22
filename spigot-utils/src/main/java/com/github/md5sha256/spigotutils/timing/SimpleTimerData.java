package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SimpleTimerData<V> implements TimerData<V> {

    private final IStopwatch stopwatch;
    private final V data;

    public SimpleTimerData(@NotNull V value, @NotNull IStopwatch stopwatch) {
        this.data = Objects.requireNonNull(value);
        this.stopwatch = Objects.requireNonNull(stopwatch);
    }

    @Override
    public @NotNull V data() {
        return this.data;
    }

    @Override
    public @NotNull IStopwatch elapsed() {
        return this.stopwatch;
    }

}
