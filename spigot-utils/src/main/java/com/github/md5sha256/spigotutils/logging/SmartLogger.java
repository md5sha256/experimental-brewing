package com.github.md5sha256.spigotutils.logging;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface SmartLogger extends ILogger {

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isDebugEnabled();

    @NotNull LogDump newDumper();

    default void infoException(@NotNull Throwable exception, @NotNull String... messages) {
        infoException(exception, ILogger.convertRaw(messages));
    }

    void infoException(@NotNull Throwable exception, @NotNull Component... messages);

    default void warnException(@NotNull Throwable exception, @NotNull String... messages) {
        warnException(exception, ILogger.convertRaw(messages));
    }

    void warnException(@NotNull Throwable exception, @NotNull Component... messages);

    default void errorException(@NotNull Throwable exception, @NotNull String... messages) {
        errorException(exception, ILogger.convertRaw(messages));
    }

    void errorException(@NotNull Throwable exception, @NotNull Component... messages);

    default void debugException(@NotNull Throwable exception, @NotNull String... messages) {
        debugException(exception, ILogger.convertRaw(messages));
    }

    void debugException(@NotNull Throwable exception, @NotNull Component... messages);

}
