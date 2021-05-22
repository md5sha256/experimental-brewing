package com.github.md5sha256.spigotutils.sql;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class DateQuery {

    private DateQuery() {
        throw new IllegalStateException("No instances of the utility class are allowed.");
    }

    public static @NotNull ExactNumberQuery before(long epochMilli) {
        return new ExactNumberQuery(epochMilli, ArithmeticComparator.LESS);
    }

    public static @NotNull ExactNumberQuery before(@NotNull Instant instant) {
        return before(instant.toEpochMilli());
    }

    public static @NotNull NumberQuery after(long epochMilli) {
        return new ExactNumberQuery(epochMilli, ArithmeticComparator.GREATER);
    }

    public static @NotNull NumberQuery after(@NotNull Instant instant) {
        return after(instant.toEpochMilli());
    }

    public static @NotNull NumberQuery at(long epochMilli) {
        return new ExactNumberQuery(epochMilli, ArithmeticComparator.EQUAL);
    }

    public static @NotNull NumberQuery at(@NotNull Instant instant) {
        return at(instant.toEpochMilli());
    }

    public static @NotNull NumberQuery between(long min, long max) {
        return new BoundedNumberQuery(min, max, false, false, true);
    }

    public static @NotNull NumberQuery between(@NotNull Instant min, @NotNull Instant max) {
        return between(min.toEpochMilli(), max.toEpochMilli());
    }

}
