package com.github.md5sha256.spigotutils.message;

import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public interface MessageKey {

    static @NotNull MessageKey create(@NotNull String keyName) {
        return new SimpleMessageKey(keyName);
    }

    static @NotNull MessageKey create(@NotNull String keyName, @NotNull String... rawPath) {
        return new SimpleMessageKey(keyName, rawPath);
    }

    @NotNull String keyName();

    @NotNull String[] rawPath();

    default @NotNull String path(CharSequence delimiter) {
        final StringJoiner joiner = new StringJoiner(delimiter);
        for (String s : rawPath()) {
            joiner.add(s);
        }
        return joiner.toString();
    }

}
