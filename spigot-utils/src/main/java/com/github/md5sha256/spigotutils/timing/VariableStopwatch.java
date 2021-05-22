package com.github.md5sha256.spigotutils.timing;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface VariableStopwatch extends IStopwatch {

    @Override
    @NotNull VariableStopwatch start();

    @Override
    @NotNull VariableStopwatch stop();

    @Override
    @NotNull VariableStopwatch reset();

    @NotNull VariableStopwatch setElapsedTime(long elapsed, @NotNull TimeUnit timeUnit);


}
