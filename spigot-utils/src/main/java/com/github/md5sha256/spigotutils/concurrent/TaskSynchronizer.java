package com.github.md5sha256.spigotutils.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public interface TaskSynchronizer {

    boolean isPrimaryThread();

    default void ensureSync() throws AsyncAccessException {
        ensureSync("Async access caught! Thread: " + Thread.currentThread().getName());
    }

    default void ensureSync(@NotNull String message) throws AsyncAccessException {
        if (!isPrimaryThread()) {
            throw new AsyncAccessException(message);
        }
    }

    @NotNull <T> CompletableFuture<T> syncGetNow(@NotNull Callable<T> callable);

    default @NotNull CompletableFuture<Void> syncNow(@NotNull Runnable runnable) {
        return syncGetNow(Executors.callable(runnable, null));
    }

    @NotNull <T> CompletableFuture<T> asyncGet(@NotNull Callable<T> callable);

    default @NotNull CompletableFuture<Void> async(@NotNull Runnable runnable) {
        return asyncGet(Executors.callable(runnable, null));
    }
}
