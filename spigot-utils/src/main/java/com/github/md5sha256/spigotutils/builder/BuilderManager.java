package com.github.md5sha256.spigotutils.builder;

import com.github.md5sha256.spigotutils.Common;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@Singleton
public class BuilderManager {

    private final Map<Class<?>, Function<String, ?>> resolverMap = new HashMap<>();

    private final Collection<Class<?>> primitives = Arrays
            .asList(String.class, Byte.class, Byte.TYPE, Short.class, Short.TYPE, Integer.class,
                    Integer.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE, Long.class,
                    Long.TYPE);

    private final Map<Class<?>, StagedBuilder<?>> builderCache = new HashMap<>();


    @Inject
    public BuilderManager() {
        registerDefaultResolver(this);
    }

    public <T> Optional<StagedBuilder<T>> getCachedBuilder(@NotNull final Class<T> clazz) {
        final Optional<StagedBuilder<T>> optional =
                Optional.ofNullable(Common.unsafeCast(builderCache.get(clazz)));
        if (optional.isPresent()) {
            return optional;
        }
        for (final Map.Entry<Class<?>, StagedBuilder<?>> entry : builderCache.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                return Optional.of(Common.unsafeCast(entry.getValue()));
            }
        }
        return Optional.empty();
    }

    public void registerBuilder(@NotNull final StagedBuilder<?> builder) {
        builderCache.put(builder.getGenericType(), builder);
    }

    public void removeBuilder(@NotNull final Class<?> clazz) {
        builderCache.remove(clazz);
    }

    public void removeBuilder(@NotNull final StagedBuilder<?> stagedBuilder) {
        builderCache.values().remove(stagedBuilder);
    }

    private void registerDefaultResolver(@NotNull final BuilderManager manager) {
        // Primitives
        manager.registerResolver(String.class, String::valueOf);
        manager.registerResolver(Byte.class, Byte::valueOf);
        manager.registerResolver(Byte.TYPE, Byte::parseByte);
        manager.registerResolver(Short.class, Short::valueOf);
        manager.registerResolver(Short.TYPE, Short::parseShort);
        manager.registerResolver(Float.class, Float::valueOf);
        manager.registerResolver(Float.TYPE, Float::parseFloat);
        manager.registerResolver(Integer.class, Integer::valueOf);
        manager.registerResolver(Integer.TYPE, Integer::parseInt);
        manager.registerResolver(Double.class, Double::valueOf);
        manager.registerResolver(Double.TYPE, Double::parseDouble);
        manager.registerResolver(Long.class, Long::valueOf);
        manager.registerResolver(Long.TYPE, Long::parseLong);
        // Minecraft Stuff
        manager.registerResolver(World.class, Bukkit::getWorld);
        manager.registerResolver(Player.class, Bukkit.getServer()::getPlayerExact);
    }

    private <T> void registerResolver(@NotNull final Class<T> clazz,
                                      @NotNull final Function<String, T> resolver) {
        resolverMap.put(clazz, resolver);
    }

    public boolean canResolve(@NotNull final Class<?> clazz) {
        return clazz.isEnum() || resolverMap.containsKey(clazz);
    }

    public <T> Function<String, T> getResolver(final Class<T> clazz) {
        if (!canResolve(clazz)) {
            throw new IllegalArgumentException(
                    "No resolver for class: " + clazz.getCanonicalName());
        } else if (clazz.isEnum()) {
            return (s) -> {
                final Enum<?> e = Enum.valueOf(clazz.asSubclass(Enum.class), s.toUpperCase());
                return clazz.cast(e);
            }; // Enum resolver
        }
        return Common.unsafeCast(resolverMap.get(clazz));
    }

}
