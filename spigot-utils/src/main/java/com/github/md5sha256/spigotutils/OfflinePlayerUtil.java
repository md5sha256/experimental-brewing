package com.github.md5sha256.spigotutils;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OfflinePlayerUtil {

    @NotNull Optional<@NotNull OfflinePlayer> getCachedOfflinePlayer(@NotNull UUID uuid);

    @NotNull Optional<@NotNull OfflinePlayer> getCachedOfflinePlayer(@NotNull String name);

    @NotNull CompletableFuture<@NotNull OfflinePlayer> getOrLookupOfflinePlayer(@NotNull String name);
}
