package com.github.md5sha256.spigotutils.timing;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BukkitTimer<K, V, T extends TimerData<V>> extends AbstractTimer<K, V, T> {

    private final AtomicReference<BukkitTask> task = new AtomicReference<>();

    public BukkitTimer(
            @NotNull Plugin plugin,
            @NotNull BukkitScheduler scheduler,
            boolean async,
            long intervalTicks
    ) {
        this(plugin, scheduler, async, intervalTicks, Collections.emptyMap());
    }

    public BukkitTimer(
            @NotNull Plugin plugin,
            @NotNull BukkitScheduler scheduler,
            boolean async,
            long intervalTicks,
            @NotNull Map<K, V> entries
    ) {
        super(entries);
        final BukkitTask bukkitTask;
        if (async) {
            bukkitTask = scheduler.runTaskTimerAsynchronously(plugin, this::update, intervalTicks, intervalTicks);
        } else {
            bukkitTask = scheduler.runTaskTimer(plugin, this::update, intervalTicks, intervalTicks);
        }
        this.task.set(bukkitTask);
    }

    @Override
    public boolean isShutdown() {
        final BukkitTask actualTask = this.task.get();
        return actualTask != null && !actualTask.isCancelled();
    }

    @Override
    public void shutdown() {
        final BukkitTask old = this.task.getAndSet(null);
        if (old != null) {
            old.cancel();
        }
    }

}
