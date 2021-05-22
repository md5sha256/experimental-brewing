package com.github.md5sha256.spigotutils.serial;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class SerialUtils {

    private static final Collection<Class<?>> acceptedBaseClasses;
    private static final String ITEM_SECTION_KEY = "ItemStack";

    static {
        acceptedBaseClasses = Arrays
            .asList(Boolean.class, Boolean.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE,
                Double.class, Double.TYPE, Float.class, Float.TYPE, Short.class, Short.TYPE,
                Byte.class, Byte.TYPE, String.class);
    }

    private SerialUtils() {
    }

    public static void init() {
        ConfigurationSerialization.registerClass(SerializableEnum.class);
        ConfigurationSerialization.registerClass(SerializableStopwatch.class);
        ConfigurationSerialization.registerClass(SerializableUUID.class);
    }

    public static void serializeMap(@NotNull final Map<?, ?> map,
        @NotNull final ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            validateClass(entry.getKey().getClass());
            validateClass(entry.getValue().getClass());
            section.set(entry.getKey().toString(), entry.getValue());
        }
    }

    public static <K, V extends ConfigurationSerializable> void deserializeMap(
        @NotNull final Class<V> valueClass, @NotNull final Map<K, V> map,
        @NotNull final Function<String, K> keyConverter,
        @NotNull final ConfigurationSection section) {
        for (final String key : section.getKeys(false)) {
            map.put(keyConverter.apply(key), section.getSerializable(key, valueClass));
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V extends ConfigurationSerializable> void deserializeMap(
        @NotNull final Class<V> valueClass, @NotNull final Map<K, V> map,
        Function<String, K> keyConverter, @NotNull final Map<String, Object> serial) {
        for (Map.Entry<String, Object> entry : serial.entrySet()) {
            map.put(keyConverter.apply(entry.getKey()), valueClass.cast(ConfigurationSerialization
                .deserializeObject((Map<String, Object>) entry.getValue())));
        }
    }

    public static <C extends ConfigurationSerializable> void serializeList(
        @NotNull final List<C> list, @NotNull final ConfigurationSection section) {
        ListIterator<C> iterator = list.listIterator();
        while (iterator.hasNext()) {
            int index = iterator.nextIndex() - 1;
            final C object = iterator.next();
            section.set(String.valueOf(index), object);
        }
    }

    public static <C extends ConfigurationSerializable> Map<String, Object> serializeList(
        @NotNull final List<C> list) {
        ListIterator<C> iterator = list.listIterator();
        Map<String, Object> map = new HashMap<>(list.size());
        while (iterator.hasNext()) {
            int index = iterator.nextIndex() - 1;
            final C object = iterator.next();
            map.put(String.valueOf(index), object);
        }
        return map;
    }

    public static <C extends ConfigurationSerializable> void deserializeList(
        @NotNull final Class<C> clazz, @NotNull final List<C> list,
        @NotNull final ConfigurationSection section) {
        int size = section.getKeys(false).size();
        list.addAll(Collections.nCopies(size, null));
        for (String key : section.getKeys(false)) {
            try {
                int index = Integer.parseInt(key);
                list.set(index, section.getSerializable(key, clazz));
            } catch (NumberFormatException ex) {
                ex.printStackTrace(); //Fail gracefully
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <C extends ConfigurationSerializable> void deserializeList(
        @NotNull final Class<C> clazz, @NotNull final List<C> list,
        @NotNull final Map<String, Object> map) {
        list.addAll(Collections.nCopies(map.size(), null));
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                int index = Integer.parseInt(entry.getKey());
                list.set(index, clazz.cast(ConfigurationSerialization
                    .deserializeObject((Map<String, Object>) entry.getValue(), clazz)));
            } catch (NumberFormatException ex) {
                ex.printStackTrace(); //Fail gracefully
            }
        }
    }

    public static <C extends ConfigurationSerializable> void serializeIterable(
        @NotNull final Iterable<C> iterable, @NotNull final ConfigurationSection section) {
        int index = 0;
        for (C object : iterable) {
            section.set(String.valueOf(index++), object);
        }
    }

    public static <C extends ConfigurationSerializable> Map<String, Object> serializeIterable(
        @NotNull final Iterable<C> iterable) {
        final Map<String, Object> map = new HashMap<>();
        int index = 0;
        for (C object : iterable) {
            map.put(String.valueOf(index++), object);
        }
        return map;
    }

    public static <C extends ConfigurationSerializable> void deserializeIterable(
        @NotNull Class<C> clazz, @NotNull final Collection<C> collection,
        @NotNull ConfigurationSection section) {
        for (String s : section.getKeys(false)) {
            C serializable = section.getSerializable(s, clazz);
            collection.add(serializable);
        }
    }

    @SuppressWarnings("unchecked")
    public static <C extends ConfigurationSerializable> void deserializeIterable(
        @NotNull final Class<C> clazz, @NotNull final Collection<C> list,
        @NotNull final Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            list.add(clazz.cast(ConfigurationSerialization
                .deserializeObject((Map<String, Object>) entry.getValue(), clazz)));
        }
    }

    @NotNull public static String toBase64(@NotNull final ItemStack itemStack) {
        final YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(ITEM_SECTION_KEY, itemStack);
        return Base64.getEncoder().encodeToString(configuration.saveToString().getBytes());
    }

    @NotNull public static ItemStack fromBase64(@NotNull final String base64) {
        final String raw = new String(Base64.getDecoder().decode(base64.getBytes()));
        final YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.loadFromString(raw);
        } catch (InvalidConfigurationException ex) {
            throw new IllegalArgumentException("Invalid Serial", ex);
        }
        final ItemStack itemStack = configuration.getItemStack(ITEM_SECTION_KEY);
        if (itemStack == null) {
            throw new IllegalArgumentException("Failed to find item stack!");
        }
        return itemStack;
    }


    private static void validateClass(Class<?> clazz) {
        if (clazz.isArray()) {
            validateClass(clazz.getComponentType());
            return;
        }
        if (!acceptedBaseClasses.contains(clazz) && !ConfigurationSerializable.class
            .isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getCanonicalName()
                + " is not accepted! Class must be a primitive, String or ConfigurationSerializable!");
        }
    }

    @NotNull public static YamlConfiguration cloneConfiguration(@NotNull final YamlConfiguration toClone) {
        final String s = toClone.saveToString();
        try {
            final YamlConfiguration ret = new YamlConfiguration();
            ret.loadFromString(s);
            return ret;
        } catch (InvalidConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
