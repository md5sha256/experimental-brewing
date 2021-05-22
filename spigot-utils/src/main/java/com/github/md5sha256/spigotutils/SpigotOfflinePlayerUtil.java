package com.github.md5sha256.spigotutils;

import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public class SpigotOfflinePlayerUtil implements OfflinePlayerUtil {

    private final Method getOfflinePlayerProfile, getNMSServer, getMinecraftServer, getProfileUUID,
            getUserCache, getProfileString;
    private final String nmsVersion;

    @Inject
    private TaskSynchronizer taskSynchronizer;

    @Inject
    public SpigotOfflinePlayerUtil() throws RuntimeException {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String[] arr = name.split("\\.");
        nmsVersion = arr[3];
        try {
            final Class<?> craftServerClass = Bukkit.getServer().getClass();
            getNMSServer = craftServerClass.getMethod("getHandle");
            final Class<?> nmsClass = Class.forName(getNMSPackageName() + "MinecraftServer");
            getMinecraftServer = nmsClass.getMethod("getServer");

            getUserCache = nmsClass.getMethod("getUserCache");
            final Class<?> userCache = Class.forName(getNMSPackageName() + "UserCache");
            final Class<?> gameProfile = Class.forName("com.mojang.authlib.GameProfile");
            getProfileUUID = userCache.getMethod("getProfile", UUID.class);
            getProfileString = userCache.getMethod("getProfile", String.class);

            getOfflinePlayerProfile = craftServerClass.getMethod("getOfflinePlayer", gameProfile);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getNMSVersion() {
        return nmsVersion;
    }

    public String getCraftBukkitPackageName() {
        return "org.bukkit.craftbukkit." + nmsVersion + ".";
    }

    public String getNMSPackageName() {
        return "net.minecraft.server." + nmsVersion + ".";
    }

    private Object getUserCache() throws ReflectiveOperationException {
        Object nmsServer = getNMSServer.invoke(Bukkit.getServer());
        nmsServer = getMinecraftServer.invoke(nmsServer);
        return getUserCache.invoke(nmsServer);
    }

    @Override
    public @NotNull Optional<OfflinePlayer> getCachedOfflinePlayer(@NotNull final UUID uuid) {
        try {
            final Object userCache = getUserCache();
            final Object profile = getProfileUUID.invoke(userCache, uuid);
            if (profile == null) {
                return Optional.empty();
            }
            final OfflinePlayer offlinePlayer =
                    (OfflinePlayer) getOfflinePlayerProfile.invoke(Bukkit.getServer(), profile); // Static method.
            return Optional.ofNullable(offlinePlayer);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public @NotNull Optional<OfflinePlayer> getCachedOfflinePlayer(@NotNull final String name) {
        try {
            final Object userCache = getUserCache();
            final Object profile = getProfileString.invoke(userCache, name);
            if (profile == null) {
                return Optional.empty();
            }
            final OfflinePlayer offlinePlayer =
                    (OfflinePlayer) getOfflinePlayerProfile.invoke(Bukkit.getServer(), profile); // Static method.
            return Optional.ofNullable(offlinePlayer);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull CompletableFuture<@NotNull OfflinePlayer> getOrLookupOfflinePlayer(@NotNull final String name) {
        final Optional<OfflinePlayer> optional = getCachedOfflinePlayer(name);
        return optional.map(CompletableFuture::completedFuture)
                .orElseGet(() -> taskSynchronizer.asyncGet(()-> Bukkit.getOfflinePlayer(name)));
    }

}
