package com.github.md5sha256.spigotutils.sql;

import org.jetbrains.annotations.NotNull;

public enum ArithmeticComparator {

    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    LESS("<"),
    LESS_OR_EQUAL("<="),
    EQUAL("="),
    NOT_EQUAL("!=");

    ArithmeticComparator(@NotNull String operand) {
        this.operand = operand;
    }

    public final @NotNull String operand;

    public @NotNull ArithmeticComparator negate() {
        switch (this) {
            case EQUAL:
                return NOT_EQUAL;
            case NOT_EQUAL:
                return EQUAL;
            case LESS:
                return GREATER;
            case GREATER:
                return LESS;
            case LESS_OR_EQUAL:
                return GREATER_OR_EQUAL;
            case GREATER_OR_EQUAL:
                return LESS_OR_EQUAL;
            default:
                throw new IllegalStateException("Unknown operation: " + this);
        }
    }

}
