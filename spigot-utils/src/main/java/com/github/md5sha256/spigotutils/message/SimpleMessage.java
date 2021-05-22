package com.github.md5sha256.spigotutils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class SimpleMessage implements IMessage {

    private final MessageKey key;
    private final Component value;
    private final String comment;

    public SimpleMessage(@NotNull MessageKey key, @NotNull Component value, @Nullable String comment) {
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
        this.comment = comment;
    }

    @Override
    public @NotNull MessageKey key() {
        return this.key;
    }

    @Override
    public @NotNull Component value() {
        return this.value;
    }


    @Override
    public @NotNull Optional<@NotNull String> comment() {
        return Optional.ofNullable(this.comment);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMessage that = (SimpleMessage) o;

        if (!key.equals(that.key)) return false;
        if (!value.equals(that.value)) return false;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "key=" + key +
                ", value=" + MiniMessage.get().serialize(this.value) +
                ", comment='" + comment + '\'' +
                '}';
    }

}
