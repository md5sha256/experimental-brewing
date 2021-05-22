package com.github.md5sha256.spigotutils.serial;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRegistry<K, T> implements Registry<K, T> {

    protected final Map<K, T> instances = new ConcurrentHashMap<>();

    @Override
    public @NotNull Optional<@NotNull T> get(@NotNull K key) {
        return Optional.ofNullable(this.instances.get(key));
    }

    @Override
    public void register(@NotNull K key, @NotNull T value) throws IllegalArgumentException {
        if (this.instances.containsKey(key)) {
            throw new IllegalArgumentException("Value already registered for key: " + key);
        }
        this.instances.put(key, value);
    }

    @Override
    public boolean containsKey(@NotNull final K key) {
        return this.instances.containsKey(key);
    }

    @Override
    public @Nullable T clear(@NotNull K key) {
        return this.instances.remove(key);
    }

    @Override
    public void clear() {
        this.instances.clear();
    }

    @Override
    public int size() {
        return this.instances.size();
    }

    @Override
    public @NotNull Set<@NotNull K> keySet() {
        return new HashSet<>(this.instances.keySet());
    }

    @Override
    public @NotNull Collection<@NotNull T> values() {
        return new ArrayList<>(this.instances.values());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRegistry<?, ?> that = (SimpleRegistry<?, ?>) o;

        return instances.equals(that.instances);
    }

    @Override
    public int hashCode() {
        return instances.hashCode();
    }

}
