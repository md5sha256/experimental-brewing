package com.github.md5sha256.spigotutils.tuple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class Pair<K, V> {

    public static final Pair<?, ?> EMPTY = new Pair<>(null, null);

    private final @Nullable K primary;
    private final @Nullable V secondary;

    private Pair(@Nullable K primary, @Nullable V secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Pair<K, V> empty() {
        return (Pair<K, V>) EMPTY;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> collect(
            @NotNull Supplier<? extends Map<K, V>> mapSupplier,
            @NotNull Pair<K, V>... pairs
    ) {
        final Map<K, V> map = mapSupplier.get();
        collect(map, pairs);
        return map;
    }

    @SafeVarargs
    public static <K, V> void collect(@NotNull Map<K, V> map, @NotNull Pair<K, V>... pairs) {
        for (Pair<K, V> pair : pairs) {
            map.put(pair.primary, pair.secondary);
        }
    }

    public static <K, V> @NotNull Pair<@Nullable K, @Nullable V> of(@Nullable K primary, @Nullable V secondary) {
        if (primary == null && secondary == null) {
            return empty();
        }
        return new Pair<>(primary, secondary);
    }


    public @Nullable K primary() {
        return this.primary;
    }

    @Contract("!null -> !null")
    public K primary(K def) {
        return this.primary == null ? def : this.primary;
    }

    public @Nullable V secondary() {
        return this.secondary;
    }

    @Contract("!null -> !null")
    public V secondary(V def) {
        return this.secondary == null ? def : this.secondary;
    }

    @Override
    public int hashCode() {
        int result = primary != null ? primary.hashCode() : 0;
        result = 31 * result + (secondary != null ? secondary.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!Objects.equals(primary, pair.primary)) {
            return false;
        }
        return Objects.equals(secondary, pair.secondary);
    }

}
