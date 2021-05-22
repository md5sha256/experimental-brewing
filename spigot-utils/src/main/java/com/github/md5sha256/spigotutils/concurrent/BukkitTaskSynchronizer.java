package com.github.md5sha256.spigotutils.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class BukkitTaskSynchronizer implements TaskSynchronizer {

    private final Plugin plugin;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    public static BukkitTaskSynchronizer create(final Plugin plugin) {
        return new BukkitTaskSynchronizer(plugin);
    }

    private BukkitTaskSynchronizer(final Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public @NotNull <T> CompletableFuture<T> syncGetNow(@NotNull Callable<T> callable) {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduler.callSyncMethod(plugin, () -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Throwable ex) {
                if (!completableFuture.isDone()) {
                    completableFuture.completeExceptionally(ex);
                }
            }
            return null;
        });
        return completableFuture;
    }

    @Override
    public @NotNull <T> CompletableFuture<T> asyncGet(@NotNull final Callable<T> callable) {
        return null;
    }
}
