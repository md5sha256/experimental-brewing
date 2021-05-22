package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTimer<K, V, T extends TimerData<V>> implements Timer<K, V, T> {

    private final Map<K, T> entries = new ConcurrentHashMap<>();


    protected AbstractTimer() {
    }

    protected AbstractTimer(@NotNull Map<K, V> entries) {
        submitEntries(entries);
    }

    protected abstract void update();

    protected abstract void onRemove(@NotNull K key, @NotNull T value);

    protected abstract @NotNull T create(@NotNull K key, @NotNull V value);

    @Override
    public @NotNull Optional<@NotNull T> data(@NotNull final K key) {
        return Optional.ofNullable(this.entries.get(key));
    }

    @Override
    public @NotNull T submit(@NotNull final K key, @NotNull final V value) {
        remove(key);
        final T data = create(key, value);
        this.entries.put(key, data);
        return data;
    }

    protected void submitEntriesRaw(@NotNull Map<K, T> entries) {
        this.entries.putAll(entries);
    }

    @Override
    public @NotNull Map<@NotNull K, T> submitEntries(@NotNull final Map<K, V> entries) {
        final Map<K, T> target = new HashMap<>(entries.size());
        for (Map.Entry<K, V> entry : entries.entrySet()) {
            target.put(entry.getKey(), create(entry.getKey(), entry.getValue()));
        }
        this.entries.putAll(target);
        return target;
    }

    @Override
    public void remove(@NotNull final K key) {
        final T data = this.entries.remove(key);
        if (data != null) {
            onRemove(key, data);
        }
    }

    @Override
    public void removeEntries(@NotNull final Collection<@NotNull K> keys) {
        for (K key : keys) {
            remove(key);
        }
    }

    @Override
    public @NotNull Set<@NotNull K> keys() {
        return new HashSet<>(this.entries.keySet());
    }

    @Override
    public @NotNull Collection<@NotNull V> values() {
        final Collection<V> values = new ArrayList<>(this.entries.values().size());
        for (TimerData<V> data : this.entries.values()) {
            values.add(data.data());
        }
        return values;
    }

    @Override
    public @NotNull Map<@NotNull K, @NotNull T> asMap() {
        return new HashMap<>(this.entries);
    }

    @Override
    public void resetValues() {
        for (TimerData<V> data : this.entries.values()) {
            data.elapsed().reset();
        }
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public void clear() {
        final Map<K, @NotNull T> copy = new HashMap<>(this.entries);
        this.entries.clear();
        for (Map.Entry<K, T> entry : copy.entrySet()) {
            onRemove(entry.getKey(), entry.getValue());
        }
    }

}
