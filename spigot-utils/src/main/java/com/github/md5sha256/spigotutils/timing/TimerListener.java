package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TimerListener<K, V, T extends TimerData<V>> {

    void onRemove(@NotNull K key, @NotNull T data);

}
