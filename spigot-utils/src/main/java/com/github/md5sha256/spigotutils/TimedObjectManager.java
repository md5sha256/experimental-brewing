package com.github.md5sha256.spigotutils;

import com.github.md5sha256.spigotutils.serial.SerializableStopwatch;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Deprecated
public class TimedObjectManager<T> implements Cloneable {

    private final Map<T, SerializableStopwatch> dataMap = new HashMap<>();
    private final long maxTimeMillis;
    private final BukkitTask task;
    private BiConsumer<T, SerializableStopwatch> removalHandler;

    TimedObjectManager(final long maxTimeMillis,
                              final long updateIntervalTicks,
                              final Plugin plugin) {
        this(maxTimeMillis, updateIntervalTicks, false, plugin);
    }

    TimedObjectManager(final long maxTimeMillis,
                               final long updateIntervalTicks,
                              final boolean async,
                              final Plugin plugin) {
        this.maxTimeMillis = maxTimeMillis;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (async) {
            this.task = scheduler
                    .runTaskTimerAsynchronously(plugin, this::update, updateIntervalTicks, updateIntervalTicks);
        } else {
            this.task = scheduler
                    .runTaskTimer(plugin, this::update, updateIntervalTicks, updateIntervalTicks);
        }
    }

    public long getElapsed(@NotNull final T t, @NotNull final TimeUnit timeUnit) {
        final SerializableStopwatch stopwatch = dataMap.get(t);
        if (stopwatch == null) {
            return 0;
        }
        return stopwatch.elapsed(timeUnit);
    }

    public void setRemovalHandler(
            @Nullable final BiConsumer<T, SerializableStopwatch> removalHandler) {
        this.removalHandler = removalHandler;
    }

    public void remove(@NotNull final T t) {
        final SerializableStopwatch stopwatch = dataMap.remove(t);
        if (stopwatch != null && removalHandler != null) {
            removalHandler.accept(t, stopwatch);
        }
    }

    public void reset(@NotNull final T t) {
        final SerializableStopwatch stopwatch = dataMap.get(t);
        if (stopwatch != null && removalHandler != null) {
            removalHandler.accept(t, stopwatch);
        }
    }

    public void clear() {
        if (removalHandler != null) {
            for (final Map.Entry<T, SerializableStopwatch> stopwatchEntry : dataMap.entrySet()) {
                removalHandler.accept(stopwatchEntry.getKey(), stopwatchEntry.getValue());
            }
        }
        dataMap.clear();
    }

    public void update() {
        for (final Map.Entry<T, SerializableStopwatch> stopwatchEntry : dataMap.entrySet()) {
            final SerializableStopwatch stopwatch = stopwatchEntry.getValue();
            if (stopwatch.elapsed(TimeUnit.MILLISECONDS) >= maxTimeMillis) {
                removalHandler.accept(stopwatchEntry.getKey(), stopwatchEntry.getValue());
            }
        }
    }

    public void registerObject(@NotNull final T object) {
        dataMap.computeIfAbsent(object, (unused) -> new SerializableStopwatch());
    }

    public boolean isRunning() {
        return !task.isCancelled();
    }

    public void close() {
        if (isRunning()) {
            task.cancel();
        }
    }

}
