package com.github.md5sha256.spigotutils.message;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public interface MessageRegistry {

    @NotNull IMessage message(@NotNull MessageKey key);

    default boolean containsKey(@NotNull MessageKey key) {
        return !message(key).empty();
    }

    void message(@NotNull IMessage message);

    void message(@NotNull Collection<IMessage> messages);

    @NotNull Map<@NotNull MessageKey, @NotNull IMessage> registered();

    @NotNull Collection<@NotNull IMessage> registeredMessages();

    int size();

    void clear();

}
