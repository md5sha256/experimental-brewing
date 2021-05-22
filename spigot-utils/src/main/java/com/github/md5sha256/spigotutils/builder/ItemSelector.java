package com.github.md5sha256.spigotutils.builder;

import com.github.md5sha256.spigotutils.Common;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemSelector implements Cloneable, ConfigurationSerializable {

    private static final String SELECTOR_KEY = "selected-item";
    private static final String LOCATION_KEY = "selected-location";

    private final ItemStack selector;
    private Location selected;

    public ItemSelector(final ItemStack wand) {
        this.selector = wand;
    }

    @NotNull
    public static ItemSelector deserialize(@NotNull final Map<String, Object> serial) {
        if (!serial.containsKey(SELECTOR_KEY)) {
            throw new IllegalArgumentException("Invalid Serial!");
        }
        final ItemSelector selector = new ItemSelector(Common.unsafeCast(serial.get(SELECTOR_KEY)));
        selector.selected = Common.unsafeCast(serial.get(LOCATION_KEY));
        return selector;
    }

    public ItemStack getSelector() {
        return selector;
    }

    @Nullable
    public Location getSelected() {
        return selected;
    }

    public void setSelected(@Nullable final Location selected) {
        this.selected = selected;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put(SELECTOR_KEY, selector);
        map.put(LOCATION_KEY, selected);
        return map;
    }

    @Override
    public ItemSelector clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        final ItemSelector selector = new ItemSelector(this.selector);
        selector.selected = selected;
        return selector;
    }
}
