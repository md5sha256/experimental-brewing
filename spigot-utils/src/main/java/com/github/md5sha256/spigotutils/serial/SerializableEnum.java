package com.github.md5sha256.spigotutils.serial;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SerializableEnum<E extends Enum<E>> implements ConfigurationSerializable {

    private static final String CLASS_KEY = "class", NAME_KEY = "value";

    private final Class<E> clazz;
    private final String name;

    public SerializableEnum(@NotNull final Enum<E> anEnum) {
        this.clazz = anEnum.getDeclaringClass();
        this.name = anEnum.name();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @NotNull public static SerializableEnum<?> deserialize(final Map<String, Object> serial) {
        if (!serial.containsKey(CLASS_KEY) || !serial.containsKey(NAME_KEY)) {
            throw new IllegalArgumentException("Invalid Serial!");
        }
        final String name = (String) serial.get(NAME_KEY);
        try {
            final Class<?> clazz = Class.forName((String) serial.get(CLASS_KEY));
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("Invalid class: " + clazz.getCanonicalName() + "! Not an enum!");
            }
            final Class<? extends Enum> casted = clazz.asSubclass(Enum.class);
            return new SerializableEnum<>(Enum.valueOf(casted, name));
        } catch (final ClassNotFoundException ex) {
            throw new IllegalArgumentException("Invalid class: " + name);
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put(CLASS_KEY, clazz.getCanonicalName());
        map.put(NAME_KEY, name);
        return map;
    }

    public Enum<E> get() {
        return Enum.valueOf(clazz, name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializableEnum<?> that = (SerializableEnum<?>) o;

        if (!clazz.equals(that.clazz)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

}
