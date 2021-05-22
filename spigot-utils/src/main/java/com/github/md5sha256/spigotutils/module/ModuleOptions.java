package com.github.md5sha256.spigotutils.module;

import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import com.github.md5sha256.spigotutils.logging.ILogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModuleOptions {

    public @NotNull final TaskSynchronizer taskSynchronizer;
    public @NotNull final Plugin plugin;
    public @NotNull final ILogger logger;

    public static @NotNull ModuleOptionsBuilder builder(@NotNull Plugin plugin) {
        return new ModuleOptionsBuilder(plugin);
    }

    ModuleOptions(@NotNull Plugin plugin, @NotNull TaskSynchronizer synchronizer, @NotNull ILogger logger) {
        this.plugin = plugin;
        this.taskSynchronizer = synchronizer;
        this.logger = logger;
    }



}
