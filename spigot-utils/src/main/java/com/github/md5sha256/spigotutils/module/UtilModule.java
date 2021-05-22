package com.github.md5sha256.spigotutils.module;

import com.github.md5sha256.spigotutils.OfflinePlayerUtil;
import com.github.md5sha256.spigotutils.PaperOfflinePlayerUtil;
import com.github.md5sha256.spigotutils.SpigotOfflinePlayerUtil;
import com.github.md5sha256.spigotutils.UtilityFactory;
import com.github.md5sha256.spigotutils.UtilityFactoryImpl;
import com.github.md5sha256.spigotutils.builder.ItemSelector;
import com.github.md5sha256.spigotutils.builder.StagedBuilder;
import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import com.github.md5sha256.spigotutils.logging.ILogger;
import com.github.md5sha256.spigotutils.logging.SmartLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class UtilModule extends AbstractModule {

    private static final Collection<Class<? extends ConfigurationSerializable>> toRegister =
            Arrays.asList(ItemSelector.class, StagedBuilder.InputData.class);

    private final ModuleOptions options;

    public UtilModule(@NotNull ModuleOptions options) {
        this.options = Objects.requireNonNull(options);
        for (Class<? extends ConfigurationSerializable> clazz : toRegister) {
            ConfigurationSerialization.registerClass(clazz);
        }
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(this.options.plugin);
        install(new BukkitPlatformModule());
        bind(UtilityFactory.class).to(UtilityFactoryImpl.class).in(Singleton.class);

        bind(LegacyComponentSerializer.class).toProvider(LegacyComponentSerializer::legacyAmpersand);

        bind(TaskSynchronizer.class).toInstance(this.options.taskSynchronizer);
        bind(ILogger.class).toInstance(this.options.logger);

        if (this.options.logger instanceof SmartLogger) {
            bind(SmartLogger.class).toInstance((SmartLogger) this.options.logger);
        }
        if (PaperLib.isPaper()) {
            bind(OfflinePlayerUtil.class).to(PaperOfflinePlayerUtil.class);
        } else {
            // Bind the spigot impl early as this utilizes reflection
            bind(OfflinePlayerUtil.class).to(SpigotOfflinePlayerUtil.class).asEagerSingleton();
        }
    }

}
