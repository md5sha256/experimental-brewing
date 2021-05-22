package com.github.md5sha256.spigotutils.message;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IMessage {

    static @NotNull IMessage empty(@NotNull MessageKey key) {
        return create(key, Component.empty(), null);
    }

    static @NotNull IMessage create(@NotNull MessageKey key, @NotNull Component value) {
        return create(key, value, null);
    }

    static @NotNull IMessage create(@NotNull MessageKey key, @NotNull Component value, @Nullable String comment) {
        return new SimpleMessage(key, value, comment);
    }

    @NotNull MessageKey key();

    @NotNull Optional<@NotNull String> comment();

    @NotNull Component value();

    default boolean empty() {
        return Component.empty().equals(value());
    }

}
