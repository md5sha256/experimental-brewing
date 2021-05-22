package com.github.md5sha256.spigotutils.test;

import com.github.md5sha256.spigotutils.serial.Registry;
import com.github.md5sha256.spigotutils.serial.SerializableEnum;
import com.github.md5sha256.spigotutils.serial.SerializableStopwatch;
import com.github.md5sha256.spigotutils.serial.SerializableUUID;
import com.github.md5sha256.spigotutils.serial.SimpleRegistry;
import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestSerialUtils {

    @Test
    public void testSerializableEnum() {
        final TimeUnit target = TimeUnit.SECONDS;
        final SerializableEnum<TimeUnit> serializableTimeUnit = new SerializableEnum<>(target);
        Assertions.assertEquals(target, serializableTimeUnit.get());
        final Map<String, Object> map = serializableTimeUnit.serialize();
        final SerializableEnum<?> deserialized = SerializableEnum.deserialize(map);
        Assertions.assertEquals(serializableTimeUnit, deserialized);
        Assertions.assertEquals(serializableTimeUnit.hashCode(), deserialized.hashCode());
        final Map<String, Object> noClassKey = new HashMap<>(map);
        noClassKey.remove("class");
        Assertions.assertThrows(IllegalArgumentException.class, () -> SerializableEnum.deserialize(noClassKey));
        final Map<String, Object> noValueKey = new HashMap<>(map);
        noValueKey.remove("value");
        Assertions.assertThrows(IllegalArgumentException.class, () -> SerializableEnum.deserialize(noValueKey));
        final Class<?> clazz = Object.class;
        final String name = clazz.getCanonicalName();
        final Map<String, Object> invalidClazz = new HashMap<>(map);
        invalidClazz.put("class", name);
        Assertions.assertThrows(IllegalArgumentException.class, () -> SerializableEnum.deserialize(invalidClazz));
        invalidClazz.put("class", "non existent class");
        Assertions.assertThrows(IllegalArgumentException.class, () -> SerializableEnum.deserialize(invalidClazz));
    }

    @Test
    public void testSerializableUUID() {
        final UUID uuid = UUID.randomUUID();
        final SerializableUUID serializableUUID = new SerializableUUID(uuid);
        Assertions.assertEquals(uuid, serializableUUID.getValue());
        final Map<String, Object> map = serializableUUID.serialize();
        final SerializableUUID deserialized = new SerializableUUID(map);
        Assertions.assertEquals(serializableUUID, deserialized);
        Assertions.assertEquals(serializableUUID.hashCode(), deserialized.hashCode());
        final Map<String, Object> invalidValue = new HashMap<>(map);
        invalidValue.remove("UUID");
        Assertions.assertThrows(IllegalArgumentException.class, ()-> new SerializableUUID(invalidValue));
    }

    @Test
    public void testSerializableStopwatch() {
        final SerializableStopwatch serializableStopwatch = new SerializableStopwatch();
        serializableStopwatch.start();
        final long start = System.currentTimeMillis();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Assertions.fail(ex);
            return;
        }
        serializableStopwatch.stop();
        serializableStopwatch.stop();
        final Map<String, Object> serial = serializableStopwatch.serialize();
        final SerializableStopwatch deserialized = new SerializableStopwatch(serial);
        Assertions.assertEquals(serializableStopwatch.elapsedNanos(), deserialized.elapsedNanos());
        final Map<String, Object> noElapsed = new HashMap<>(serial);
        noElapsed.remove("elapsed");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SerializableStopwatch(noElapsed));
        final Map<String, Object> noRunning = new HashMap<>(serial);
        noRunning.remove("is-running");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SerializableStopwatch(noRunning));
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        final SerializableStopwatch unstarted = new SerializableStopwatch();
        Assertions.assertEquals(unstarted.elapsedNanos(), new SerializableStopwatch(stopwatch).elapsedNanos());
        stopwatch.start().stop();
    }

    @Test
    public void testSimpleRegistry() {
        int i = new Random().nextInt();
        final UUID value = UUID.randomUUID();
        final Registry<Integer, UUID> registry = new SimpleRegistry<>();
        final SimpleRegistry<Integer, UUID> copy = new SimpleRegistry<>();

        Assertions.assertEquals(registry.size(), 0);
        Assertions.assertFalse(registry.get(i).isPresent());
        Assertions.assertNull(registry.getIfPresent(i));
        registry.register(i, value);
        copy.register(i, value);
        Assertions.assertEquals(copy, registry);
        Assertions.assertEquals(copy.hashCode(), registry.hashCode());
        Assertions.assertThrows(IllegalArgumentException.class, () -> registry.register(i, value));
        Assertions.assertEquals(registry.size(), 1);
        Assertions.assertTrue(registry.containsKey(i));
        Assertions.assertTrue(registry.get(i).isPresent());
        Assertions.assertEquals(registry.getIfPresent(i), value);
        Assertions.assertTrue(registry.keySet().contains(i));
        Assertions.assertTrue(registry.values().contains(value));
        registry.keySet().clear();
        registry.values().clear();
        Assertions.assertTrue(registry.keySet().contains(i));
        Assertions.assertTrue(registry.values().contains(value));
        final UUID other = UUID.randomUUID();
        registry.clear(i);
        Assertions.assertFalse(registry.containsKey(i));
        Assertions.assertEquals(0, registry.size());
        registry.register(i, other);
        Assertions.assertFalse(registry.isEmpty());
        registry.clear();
        Assertions.assertEquals(0, registry.size());
    }
}
