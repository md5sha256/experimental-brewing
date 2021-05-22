package com.github.md5sha256.spigotutils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class UtilityFactoryImpl implements UtilityFactory {

    @Inject
    private Plugin plugin;

    @Override
    public @NotNull <T> TimedObjectManager<T> newObjectManager(
            final long maxTimeMillis,
            final long updateIntervalTicks
    ) {
        return new TimedObjectManager<>(maxTimeMillis, updateIntervalTicks, plugin);
    }

    @Override
    public @NotNull <T> TimedObjectManager<T> newObjectManager(
            final long maxTimeMillis,
            final long updateIntervalTicks,
            final boolean async
    ) {
        return new TimedObjectManager<>(maxTimeMillis, updateIntervalTicks, async, plugin);
    }

}
