package com.github.md5sha256.spigotutils.serial;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SerializableUUID implements ConfigurationSerializable {

    private final UUID value;

    public SerializableUUID(@NotNull final UUID uuid) {
        this.value = uuid;
    }

    public SerializableUUID(@NotNull final Map<String, Object> serial) {
        if (!serial.containsKey("UUID")) {
            throw new IllegalArgumentException("Invalid UUID!");
        }
        this.value = UUID.fromString((String) serial.get("UUID"));
    }

    public UUID getValue() {
        return value;
    }

    @Override public @NotNull Map<String, Object> serialize() {
        return Collections.singletonMap("UUID", value.toString());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializableUUID that = (SerializableUUID) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

}
