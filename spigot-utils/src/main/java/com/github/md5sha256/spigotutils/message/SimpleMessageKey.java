package com.github.md5sha256.spigotutils.message;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class SimpleMessageKey implements MessageKey {

    private static final String[] EMPTY = new String[0];

    private final String keyName;
    private final String[] rawPath;
    private final String compressedPath;

    public SimpleMessageKey(@NotNull String keyName) {
        this(keyName, keyName);
    }

    public SimpleMessageKey(@NotNull String keyName, @NotNull String... path) {
        this.keyName = Objects.requireNonNull(keyName);
        if (path.length > 0) {
            this.rawPath = Arrays.copyOf(path, path.length);
        } else {
            this.rawPath = EMPTY;
        }
        this.compressedPath = String.join(".", this.rawPath);
    }

    @Override
    public @NotNull String keyName() {
        return this.keyName;
    }

    @NotNull
    @Override
    public String[] rawPath() {
        if (this.rawPath == EMPTY) {
            return EMPTY;
        }
        return Arrays.copyOf(this.rawPath, this.rawPath.length);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleMessageKey that = (SimpleMessageKey) o;

        if (!keyName.equals(that.keyName)) {
            return false;
        }
        return this.compressedPath.equals(that.compressedPath);
    }

    @Override
    public int hashCode() {
        int result = keyName.hashCode();
        result = 31 * result + compressedPath.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SimpleMessageKey{" +
                "keyName='" + keyName + '\'' +
                ", path=" + compressedPath +
                '}';
    }

}
