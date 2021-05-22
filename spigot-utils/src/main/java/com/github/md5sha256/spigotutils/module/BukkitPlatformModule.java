package com.github.md5sha256.spigotutils.module;

import com.google.inject.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitPlatformModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BukkitScheduler.class).toInstance(Bukkit.getScheduler());
        bind(Server.class).toInstance(Bukkit.getServer());
        bind(PluginManager.class).toInstance(Bukkit.getPluginManager());
        bind(ServicesManager.class).toInstance(Bukkit.getServicesManager());
        bind(ItemFactory.class).toInstance(Bukkit.getItemFactory());
    }

}
