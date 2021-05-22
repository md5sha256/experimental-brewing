package com.github.md5sha256.spigotutils;

import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public class PaperOfflinePlayerUtil implements OfflinePlayerUtil {

    @Inject
    private TaskSynchronizer taskSynchronizer;

    @Override
    public @NotNull Optional<@NotNull OfflinePlayer> getCachedOfflinePlayer(@NotNull final UUID uuid) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (!offlinePlayer.hasPlayedBefore()) {
            return Optional.empty();
        }
        return Optional.of(offlinePlayer);
    }

    @Override
    public @NotNull Optional<@NotNull OfflinePlayer> getCachedOfflinePlayer(@NotNull final String name) {
        return Optional.ofNullable(Bukkit.getOfflinePlayerIfCached(name));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull CompletableFuture<@NotNull OfflinePlayer> getOrLookupOfflinePlayer(@NotNull final String name) {
        Optional<OfflinePlayer> optional = getCachedOfflinePlayer(name);
        return optional.map(CompletableFuture::completedFuture)
                       .orElseGet(() -> taskSynchronizer
                               .asyncGet(() -> Bukkit.getOfflinePlayer(name)));
    }

}
