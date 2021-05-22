package com.github.md5sha256.spigotutils.builder;

import com.github.md5sha256.spigotutils.serial.SerialUtils;
import com.github.md5sha256.spigotutils.serial.SerializableUUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@Singleton
public class ItemSelectorManager implements Listener {

    private final Map<UUID, SelectorData> dataMap = new HashMap<>();
    private final File file;

    private boolean cacheAll;

    @Inject
    public ItemSelectorManager(@NotNull Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "selector_data.yml");
    }

    public SelectorData getOrCreateFor(@NotNull final UUID player) {
        return dataMap.computeIfAbsent(player, SelectorData::new);
    }

    public Optional<SelectorData> getFor(@NotNull final UUID player) {
        return Optional.ofNullable(dataMap.get(player));
    }

    public void clear() {
        dataMap.clear();
    }

    public boolean clear(boolean clearDisk) {
        dataMap.clear();
        if (clearDisk) {
            return file.delete();
        }
        return true;
    }


    public static class SelectorData implements ConfigurationSerializable {

        private static final String PLAYER_KEY = "player";
        private static final String DATA_KEY = "selectors";

        private final UUID player;
        private final Collection<ItemSelector> selectors = new HashSet<>();

        public SelectorData(@NotNull final UUID player) {
            this.player = player;
        }

        private SelectorData(@NotNull final Map<String, Object> serial) {
            if (!serial.containsKey(PLAYER_KEY)) {
                throw new IllegalArgumentException("Invalid Serial!");
            }
            this.player = ((SerializableUUID) serial.get(PLAYER_KEY)).getValue();
            final Object o = serial.get(DATA_KEY);
            if (o != null) {
                SerialUtils.deserializeIterable(ItemSelector.class, selectors, (Map<String, Object>) o);
            }
        }

        public static SelectorData deserialize(@NotNull final Map<String, Object> map) {
            return new SelectorData(map);
        }

        public UUID getPlayer() {
            return player;
        }

        public Collection<ItemSelector> getSelectors() {
            return selectors;
        }

        @Override
        public @NotNull Map<String, Object> serialize() {
            final Map<String, Object> map = new HashMap<>();
            map.put(PLAYER_KEY, new SerializableUUID(player));
            map.put(DATA_KEY, SerialUtils.serializeIterable(selectors));
            return map;
        }
    }

}
