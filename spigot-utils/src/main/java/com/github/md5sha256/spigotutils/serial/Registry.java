package com.github.md5sha256.spigotutils.serial;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Registry which holds instances of objects which are effectively constant.
 *
 * @param <T> The type of objects this registry holds
 */
public interface Registry<K, T> {

    @NotNull Optional<@NotNull T> get(@NotNull K key);

    default @Nullable T getIfPresent(@NotNull K key) {
        return get(key).orElse(null);
    }

    void register(@NotNull K key, @NotNull T value) throws IllegalArgumentException;

    @Nullable T clear(@NotNull K key);

    void clear();

    boolean containsKey(@NotNull K key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    @NotNull Set<@NotNull K> keySet();

    @NotNull Collection<@NotNull T> values();

}
