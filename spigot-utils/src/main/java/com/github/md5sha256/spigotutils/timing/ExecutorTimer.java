package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ExecutorTimer<K, V, T extends TimerData<V>> extends AbstractTimer<K, V, T> {

    private final ScheduledExecutorService executor;

    public ExecutorTimer(
            @NotNull ScheduledExecutorService executor,
            long interval,
            @NotNull TimeUnit timeUnit
    ) {
        this(executor, interval, timeUnit, Collections.emptyMap());
    }

    public ExecutorTimer(
            @NotNull ScheduledExecutorService executor,
            long interval,
            @NotNull TimeUnit timeUnit,
            @NotNull Map<K, V> entries
    ) {
        super(entries);
        this.executor = executor;
        this.executor.scheduleWithFixedDelay(this::update, interval, interval, timeUnit);
    }

    @Override
    public void shutdown() {
        this.executor.shutdownNow();
        super.clear();
    }

    @Override
    public boolean isShutdown() {
        return this.executor.isShutdown();
    }

}
