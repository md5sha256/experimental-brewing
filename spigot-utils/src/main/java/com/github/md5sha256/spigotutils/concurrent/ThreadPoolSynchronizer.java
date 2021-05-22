package com.github.md5sha256.spigotutils.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ThreadPoolSynchronizer implements TaskSynchronizer {

    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    private final Plugin plugin;
    private final ExecutorService asyncExecutor;

    private ThreadPoolSynchronizer(@NotNull Plugin plugin, @NotNull ExecutorService executorService) {
        this.plugin = plugin;
        this.asyncExecutor = executorService;
    }

    public static ThreadPoolSynchronizer create(@NotNull Plugin plugin, @NotNull ExecutorService executorService) {
        return new ThreadPoolSynchronizer(plugin, executorService);
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public @NotNull <T> CompletableFuture<T> syncGetNow(@NotNull final Callable<T> callable) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        scheduler.callSyncMethod(plugin, () -> {
            try {
                future.complete(callable.call());
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
            return null;
        });
        return future;
    }

    @Override
    public @NotNull <T> CompletableFuture<T> asyncGet(@NotNull final Callable<T> callable) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        asyncExecutor.execute(() -> {
            try {
                future.complete(callable.call());
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

}
