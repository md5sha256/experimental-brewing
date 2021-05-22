package com.github.md5sha256.spigotutils.module;

import com.github.md5sha256.spigotutils.concurrent.BukkitTaskSynchronizer;
import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import com.github.md5sha256.spigotutils.logging.AudienceLoggerAdapter;
import com.github.md5sha256.spigotutils.logging.ILogger;
import com.github.md5sha256.spigotutils.logging.PluginLoggerAdapter;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ModuleOptionsBuilder {

    private final Plugin plugin;
    private TaskSynchronizer taskSynchronizer;
    private ILogger logger;

    ModuleOptionsBuilder(@NotNull Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null!");
    }

    public @NotNull ModuleOptionsBuilder taskSynchronizer(@NotNull TaskSynchronizer taskSynchronizer) {
        this.taskSynchronizer = taskSynchronizer;
        return this;
    }

    public @NotNull ModuleOptionsBuilder logger(@NotNull ILogger logger) {
        this.logger = logger;
        return this;
    }

    private void validate() throws IllegalArgumentException {
        if (this.taskSynchronizer == null) {
            this.taskSynchronizer = BukkitTaskSynchronizer.create(plugin);
        }
        if (this.logger == null) {
            this.logger = PluginLoggerAdapter.adapt(this.taskSynchronizer, this.plugin.getLogger());
            if (PaperLib.isPaper()) {
                final String raw = "[%s]";
                final Component prefix = Component.text(String.format(raw, this.plugin.getName()));
                this.logger = AudienceLoggerAdapter.adapt(
                        // FIXME
                        Level.INFO,
                        prefix,
                        Bukkit.getConsoleSender(),
                        this.taskSynchronizer
                );
            }
        }
    }

    public @NotNull ModuleOptions build() {
        validate();
        return new ModuleOptions(this.plugin, this.taskSynchronizer, this.logger);
    }

}
