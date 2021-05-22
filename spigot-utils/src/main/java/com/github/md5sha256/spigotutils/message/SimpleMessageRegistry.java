package com.github.md5sha256.spigotutils.message;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMessageRegistry implements MessageRegistry {

    private final Map<MessageKey, IMessage> messageMap = new ConcurrentHashMap<>();

    public SimpleMessageRegistry() {

    }

    public SimpleMessageRegistry(@NotNull Map<MessageKey, IMessage> registered) {
        this.messageMap.putAll(registered);
    }

    @Override
    public @NotNull IMessage message(@NotNull final MessageKey key) {
        return this.messageMap.getOrDefault(key, IMessage.empty(key));
    }

    @Override
    public void message(@NotNull final IMessage message) {
        this.messageMap.put(message.key(), message);
    }

    @Override
    public void message(@NotNull final Collection<IMessage> messages) {
        final Map<MessageKey, IMessage> map = new HashMap<>(messages.size());
        for (IMessage message : messages) {
            map.put(message.key(), message);
        }
        this.messageMap.putAll(map);
    }

    @Override
    public @NotNull Map<@NotNull MessageKey, @NotNull IMessage> registered() {
        return new HashMap<>(this.messageMap);
    }

    @Override
    public @NotNull Collection<@NotNull IMessage> registeredMessages() {
        return new HashSet<>(this.messageMap.values());
    }

    @Override
    public int size() {
        return this.messageMap.size();
    }

    @Override
    public void clear() {
        this.messageMap.clear();
    }

}
