package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

public interface TimerData<V> {

    @NotNull V data();

    @NotNull IStopwatch elapsed();

}
