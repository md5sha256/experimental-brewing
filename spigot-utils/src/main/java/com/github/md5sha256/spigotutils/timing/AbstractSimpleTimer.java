package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractSimpleTimer<K, V> extends AbstractTimer<K, V, SimpleTimerData<V>> {

    protected AbstractSimpleTimer() {
    }

    protected AbstractSimpleTimer(final @NotNull Map<K, V> entries) {
        super(entries);
    }

    protected abstract @NotNull IStopwatch newStopwatch();

    @Override
    protected final @NotNull SimpleTimerData<V> create(@NotNull final K key, @NotNull final V value) {
        return new SimpleTimerData<>(value, newStopwatch());
    }

}
