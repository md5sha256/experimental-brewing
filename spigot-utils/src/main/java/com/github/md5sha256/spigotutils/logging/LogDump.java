package com.github.md5sha256.spigotutils.logging;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LogDump extends ILogger {

    @NotNull List<@NotNull Component> accumulated();

    void dump();

}
