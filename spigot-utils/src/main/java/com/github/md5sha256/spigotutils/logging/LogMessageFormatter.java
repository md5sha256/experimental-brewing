package com.github.md5sha256.spigotutils.logging;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LogMessageFormatter {

    static LogMessageFormatter identity() {
        return component -> component;
    }

    @NotNull Component transform(Component component);

}
