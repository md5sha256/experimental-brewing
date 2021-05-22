package com.github.md5sha256.spigotutils.builder;

import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Singleton
public class SelectorManager implements Listener {

    private final Collection<ItemSelector> selectors = new HashSet<>();
    private final Map<UUID, Collection<ItemSelector>> selectorMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerClick(final PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        final Location location =
                event.getClickedBlock() == null ? null : event.getClickedBlock().getLocation();
        if (location == null) {
            return;
        }
        Optional<ItemSelector> optional = getSelector(event.getItem());
        if (!optional.isPresent()) {
            return;
        }
        event.setCancelled(true);
        final Collection<ItemSelector> selectors = selectorMap
                .computeIfAbsent(event.getPlayer().getUniqueId(), (unused) -> new HashSet<>());
        for (ItemSelector selector : selectors) {
            if (selector.getSelector().isSimilar(event.getItem())) {
                selector.setSelected(location);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        getSelector(event.getItemDrop().getItemStack())
                .ifPresent((unused) -> event.setCancelled(true));
    }

    public Optional<ItemSelector> getSelector(@NotNull final ItemStack itemStack) {
        return selectors.stream().filter(selector -> selector.getSelector().isSimilar(itemStack))
                .findFirst();
    }

    @Nullable
    public Collection<ItemSelector> getSelections(final UUID player) {
        return selectorMap.remove(player);
    }

    @Nullable
    public ItemSelector markCompleted(final UUID player, final ItemSelector selector) {
        final Collection<ItemSelector> selectors = selectorMap.get(player);
        if (selectors == null) {
            return null;
        }
        selectors.remove(selector);
        if (selectors.size() == 0) {
            selectorMap.remove(player);
        }
        return selector;
    }

    public void registerSelector(@NotNull final ItemSelector selector) {
        selectors.add(selector);
    }

    public boolean isSelector(final ItemStack itemStack) {
        return getSelector(itemStack).isPresent();
    }

    public void unregisterSelector(final ItemStack itemStack) {
        getSelector(itemStack).ifPresent(selector -> {
            selectorMap.keySet().forEach(uuid -> markCompleted(uuid, selector));
            selectors.remove(selector);
        });
    }

}
