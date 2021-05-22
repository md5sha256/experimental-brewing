package com.github.md5sha256.spigotutils.logging;

import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;

public class AudienceLoggerAdapter {

    private AudienceLoggerAdapter() {
    }

    public static @NotNull SmartLogger adapt(
            final Level level,
            final Component prefix,
            final Audience audience,
            final TaskSynchronizer synchronizer,
            final ExceptionParser exceptionParser,
            final Map<Level, LogMessageFormatter> formatterMap
    ) {
        return new AudienceLoggingBridge(level, prefix, audience, exceptionParser, formatterMap, synchronizer);
    }

    public static @NotNull SmartLogger adapt(
            final Level level,
            final Component prefix,
            final Audience audience,
            final TaskSynchronizer synchronizer
    ) {
        return new AudienceLoggingBridge(level, prefix, audience, Formatters.defaultExceptionParser(),
               Formatters.defaults(), synchronizer
        );
    }

    private static class AudienceLoggingBridge implements SmartLogger {

        private static final Function<Level, LogMessageFormatter> IDENTITY = (unused) -> LogMessageFormatter.identity();

        private final Map<Level, LogMessageFormatter> formatterMap;
        private final TaskSynchronizer synchronizer;
        private final Audience audience;
        private final Component prefix;
        private final ExceptionParser parser;
        private final Level level;

        public AudienceLoggingBridge(
                final Level level,
                final Component prefix,
                final Audience audience,
                final ExceptionParser exceptionParser,
                final Map<Level, LogMessageFormatter> formatterMap,
                final TaskSynchronizer synchronizer
        ) {
            this.level = Objects.requireNonNull(level);
            this.synchronizer = Objects.requireNonNull(synchronizer);
            this.audience = Objects.requireNonNull(audience);
            this.prefix = Objects.requireNonNull(prefix).append(Component.space());
            this.parser = Objects.requireNonNull(exceptionParser);
            final Map<Level, LogMessageFormatter> copy = new HashMap<>(formatterMap);
            copy.computeIfAbsent(Level.INFO, IDENTITY);
            copy.computeIfAbsent(Level.WARNING, IDENTITY);
            copy.computeIfAbsent(Level.SEVERE, IDENTITY);
            copy.computeIfAbsent(Level.FINE, IDENTITY);
            this.formatterMap = Collections.unmodifiableMap(copy);
        }

        private void log(@NotNull Level level, @NotNull Component... messages) {
            final LogMessageFormatter formatter = this.formatterMap.get(level);
            for (Component message : messages) {
                audience.sendMessage(prefix.append(formatter.transform(message)));
            }
        }

        private void log(@NotNull Level level, @NotNull String... messages) {
            final Component[] components = new Component[messages.length];
            for (int i = 0; i < messages.length; i++) {
                components[i] = Component.text(messages[i]);
            }
            log(level, components);
        }

        @Override
        public boolean isInfoEnabled() {
            return isLoggable(Level.INFO);
        }

        @Override
        public boolean isWarnEnabled() {
            return isLoggable(Level.WARNING);
        }

        @Override
        public boolean isErrorEnabled() {
            return isLoggable(Level.SEVERE);
        }

        @Override
        public boolean isDebugEnabled() {
            return isLoggable(Level.FINE);
        }

        @Override
        public @NotNull LogDump newDumper() {
            return new LogDumpImpl(synchronizer, this);
        }

        @Override
        public void infoException(@NotNull Throwable exception, @NotNull Component... messages) {
            final LogDump dump = newDumper();
            parser.parse(exception, dump).info(messages);
            dump.dump();
        }

        @Override
        public void warnException(@NotNull Throwable exception, @NotNull Component... messages) {
            final LogDump dump = newDumper();
            parser.parse(exception, dump).warn(messages);
            dump.dump();
        }

        @Override
        public void errorException(@NotNull Throwable exception, @NotNull Component... messages) {
            final LogDump dump = newDumper();
            parser.parse(exception, dump).error(messages);
            dump.dump();
        }

        @Override
        public void debugException(@NotNull Throwable exception, @NotNull Component... messages) {
            final LogDump dump = newDumper();
            parser.parse(exception, dump).debug(messages);
            dump.dump();
        }

        @Override
        public void plain(@NotNull String... messages) {
            log(Level.ALL, messages);
        }

        @Override
        public void info(Component... messages) {
            log(Level.INFO, messages);
        }

        @Override
        public void debug(@NotNull Component... messages) {
            log(Level.FINE, messages);
        }

        @Override
        public void warn(@NotNull Component... messages) {
            log(Level.WARNING, messages);
        }

        @Override
        public void error(@NotNull Component... messages) {
            log(Level.SEVERE, messages);
        }

        private boolean isLoggable(Level level) {
            int levelValue = this.level.intValue();
            return level.intValue() >= levelValue && levelValue != Level.OFF.intValue();
        }

    }

}
