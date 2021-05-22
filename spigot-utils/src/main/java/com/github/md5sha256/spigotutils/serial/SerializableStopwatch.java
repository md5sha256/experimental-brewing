package com.github.md5sha256.spigotutils.serial;

import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.IStopwatch;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import com.google.common.base.Stopwatch;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SerializableStopwatch implements ConfigurationSerializable, VariableStopwatch {

    private static final String ELAPSED_KEY = "elapsed", RUNNING_KEY = "is-running";

    private final IStopwatch stopwatch = GuavaAdapter.ofStopwatch(Stopwatch.createUnstarted());
    private long elapsedNanos = 0;

    public SerializableStopwatch() {
    }

    public SerializableStopwatch(long elapsed, @NotNull TimeUnit timeUnit) {
        this.elapsedNanos = timeUnit.toMillis(elapsed);
    }

    public SerializableStopwatch(@NotNull final Stopwatch stopwatch) {
        this.elapsedNanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
    }

    public SerializableStopwatch(@NotNull final Map<String, Object> serial) {
        checkKeys(serial);
        this.elapsedNanos = ((Number) serial.get(ELAPSED_KEY)).longValue();
        boolean running = (boolean) serial.get(RUNNING_KEY);
        if (running) {
            stopwatch.start();
        }
    }

    public SerializableStopwatch(final SerializableStopwatch other) {
        this.elapsedNanos = other.elapsedNanos;
        if (other.stopwatch.isRunning()) {
            this.stopwatch.start();
        }
    }

    private static void checkKeys(@NotNull final Map<String, Object> serial) {
        if (!serial.containsKey(ELAPSED_KEY) || !serial.containsKey(RUNNING_KEY)) {
            throw new IllegalArgumentException("Invalid Serial!");
        }
    }

    @Override
    public @NotNull SerializableStopwatch start() {
        if (!this.stopwatch.isRunning()) {
            this.stopwatch.start();
        }
        return this;
    }

    @Override
    public boolean isRunning() {
        return this.stopwatch.isRunning();
    }

    @Override
    public @NotNull SerializableStopwatch stop() {
        this.elapsedNanos = this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        this.stopwatch.reset();
        return this;
    }

    @Override
    public long elapsed(final TimeUnit timeUnit) {
        return timeUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public long elapsedNanos() {
        return this.elapsedNanos + this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
    }

    @Override
    public @NotNull SerializableStopwatch reset() {
        this.elapsedNanos = 0;
        this.stopwatch.reset();
        return this;
    }

    @Override
    public @NotNull SerializableStopwatch setElapsedTime(final long duration, @NotNull final TimeUnit timeUnit) {
        this.elapsedNanos = timeUnit.toMillis(duration);
        if (this.stopwatch.isRunning()) {
            this.stopwatch.reset().start();
        } else {
            this.stopwatch.reset();
        }
        return this;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put(ELAPSED_KEY, elapsedMillis());
        map.put(RUNNING_KEY, stopwatch.isRunning());
        return map;
    }

}
