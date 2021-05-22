package com.github.md5sha256.spigotutils.sql;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExactNumberQuery implements NumberQuery {

    public final @NotNull Number value;

    public final @NotNull ArithmeticComparator comparator;

    private final String contraint;

    public ExactNumberQuery(@NotNull Number value, @NotNull ArithmeticComparator comparator) {
        this.value = Objects.requireNonNull(value);
        this.comparator = Objects.requireNonNull(comparator);
        this.contraint = String.format("%s %f", comparator.operand, value.doubleValue());
    }

    @Override
    public @NotNull String generateConstraints() {
        return this.contraint;
    }

    @Override
    public boolean test(@NotNull Number number) {
        final double val = value.doubleValue();
        final double target = number.doubleValue();
        switch (comparator) {
            case GREATER:
                return target > val;
            case GREATER_OR_EQUAL:
                return target >= val;
            case LESS:
                return target < val;
            case LESS_OR_EQUAL:
                return target <= val;
            case EQUAL:
                return target == val;
            case NOT_EQUAL:
                return target != val;
            default:
                throw new IllegalStateException("Unknown comparator: " + comparator);
        }
    }

    @Override
    public @NotNull NumberQuery negate() {
        return new ExactNumberQuery(this.value, this.comparator.negate());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExactNumberQuery that = (ExactNumberQuery) o;

        if (!value.equals(that.value)) return false;
        return comparator == that.comparator;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + comparator.hashCode();
        return result;
    }

}
