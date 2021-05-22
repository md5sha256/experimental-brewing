package com.github.md5sha256.spigotutils.logging;

import com.github.md5sha256.spigotutils.Common;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.jetbrains.annotations.NotNull;

public interface ILogger {

    static @NotNull String[] toLegacyString(@NotNull Component[] components) {
        final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        final String[] arr = new String[components.length];
        for (int i = 0; i < components.length; i++) {
            arr[i] = serializer.serialize(components[i]);
        }
        return arr;
    }

    static @NotNull Component[] convertRaw(@NotNull String... messages) {
        LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();
        final Component[] arr = new Component[messages.length];
        for (int i = 0; i < messages.length; i++) {
            if (Common.isLegacyText(messages[i])) {
                arr[i] = legacyComponentSerializer.deserialize(messages[i]);
            } else {
                arr[i] = Component.text(messages[i]);
            }
        }
        return arr;
    }

    static void format(@NotNull Component[] components, @NotNull LogMessageFormatter formatter) {
        for (int i = 0; i < components.length; i++) {
            components[i] = formatter.transform(components[i]);
        }
    }

    void plain(@NotNull String... messages);

    default void plain(@NotNull Component... messages) {
        final String[] arr = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            final Component component = messages[i];
            arr[i] = PlainComponentSerializer.plain().serialize(component);
        }
        plain(arr);
    }

    void info(@NotNull Component... messages);

    default void info(@NotNull String... messages) {
        info(convertRaw(messages));
    }

    void debug(@NotNull Component... messages);

    default void debug(@NotNull String... messages) {
        debug(convertRaw(messages));
    }

    void warn(@NotNull Component... messages);

    default void warn(@NotNull String... messages) {
        warn(convertRaw(messages));
    }

    void error(@NotNull Component... messages);

    default void error(@NotNull String... messages) {
        error(convertRaw(messages));
    }

}
