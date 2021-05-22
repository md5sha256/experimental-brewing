package com.github.md5sha256.spigotutils;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface DeregisterableListener extends Listener {

    default void unregisterEvents() {
        HandlerList.unregisterAll(this);
    }

}
