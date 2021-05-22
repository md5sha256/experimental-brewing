package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Timer<K, V, T extends TimerData<V>> {

    default boolean containsKey(@NotNull K key) {
        return data(key).isPresent();
    }

    @NotNull Optional<T> data(@NotNull K key);

    @NotNull T submit(@NotNull K key, @NotNull V value);

    @NotNull Map<@NotNull K, T> submitEntries(@NotNull Map<K, V> entries);

    void remove(@NotNull K key);

    void removeEntries(@NotNull Collection<@NotNull K> keys);

    @NotNull Set<@NotNull K> keys();

    @NotNull Collection<V> values();

    @NotNull Map<@NotNull K, T> asMap();

    void resetValues();

    int size();

    void clear();

    void shutdown();

    boolean isShutdown();

}
