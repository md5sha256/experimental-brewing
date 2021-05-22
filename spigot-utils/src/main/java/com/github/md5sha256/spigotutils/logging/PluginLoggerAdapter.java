package com.github.md5sha256.spigotutils.logging;

import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoggerAdapter {

    private PluginLoggerAdapter() {
    }

    public static @NotNull SmartLogger adapt(final TaskSynchronizer synchronizer,
                                             final Logger logger,
                                             final ExceptionParser exceptionParser,
                                             final Map<Level, LogMessageFormatter> formatterMap) {
        return new PluginLoggerBridge(synchronizer, logger, exceptionParser, formatterMap);
    }

    public static @NotNull SmartLogger adapt(final TaskSynchronizer synchronizer, final Logger logger) {
        return adapt(synchronizer, logger, Formatters.defaultExceptionParser(), Formatters.defaults());
    }

    private static class PluginLoggerBridge implements SmartLogger {

        private static final Function<Level, LogMessageFormatter> IDENTITY = (unused) -> LogMessageFormatter.identity();

        private final Map<Level, LogMessageFormatter> formatterMap;
        private final TaskSynchronizer synchronizer;
        private final Logger logger;
        private final ExceptionParser parser;

        public PluginLoggerBridge(final TaskSynchronizer synchronizer,
                                  final Logger logger,
                                  final ExceptionParser exceptionParser,
                                  final Map<Level, LogMessageFormatter> formatterMap) {
            this.synchronizer = Objects.requireNonNull(synchronizer);
            this.logger = Objects.requireNonNull(logger);
            this.parser = Objects.requireNonNull(exceptionParser);
            final Map<Level, LogMessageFormatter> copy = new HashMap<>(formatterMap);
            copy.computeIfAbsent(Level.INFO, IDENTITY);
            copy.computeIfAbsent(Level.WARNING, IDENTITY);
            copy.computeIfAbsent(Level.SEVERE, IDENTITY);
            copy.computeIfAbsent(Level.FINE, IDENTITY);
            this.formatterMap = Collections.unmodifiableMap(copy);
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

        private void log(@NotNull Level level, @NotNull Component... messages) {
            final LogMessageFormatter formatter = formatterMap.get(Level.INFO);
            ILogger.format(messages, formatter);
            for (String s : ILogger.toLegacyString(messages)) {
                logger.log(level, s);
            }
        }

        private void log(@NotNull Level level, @NotNull String... messages) {
            for (String s : messages) {
                logger.log(level, s);
            }
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isLoggable(Level.INFO);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isLoggable(Level.WARNING);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isLoggable(Level.SEVERE);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isLoggable(Level.FINE);
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
    }

}
