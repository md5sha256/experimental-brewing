package com.github.md5sha256.spigotutils.logging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Formatters {

    private static final ExceptionParser DEFAULT_EXCEPTION_PARSER;

    private static final Map<Level, LogMessageFormatter> DEFAULT_FORMATTERS;
    private static final Map<Level, LogMessageFormatter> EMPTY;
    private static final LogMessageFormatter WARNING = component -> wrapColor(component, NamedTextColor.YELLOW);
    private static final LogMessageFormatter ERROR = component -> wrapColor(component, NamedTextColor.RED);

    static {
        // Init empty;
        Map<Level, LogMessageFormatter> empty = new HashMap<>(4);
        empty.put(Level.INFO, LogMessageFormatter.identity());
        empty.put(Level.WARNING, LogMessageFormatter.identity());
        empty.put(Level.SEVERE, LogMessageFormatter.identity());
        empty.put(Level.FINE, LogMessageFormatter.identity());
        EMPTY = Collections.unmodifiableMap(empty);
        //Init defaults
        Map<Level, LogMessageFormatter> temp = new HashMap<>(empty);
        temp.put(Level.WARNING, WARNING);
        temp.put(Level.SEVERE, ERROR);
        DEFAULT_FORMATTERS = Collections.unmodifiableMap(temp);

        final Pattern pattern = Pattern.compile("\n");
        DEFAULT_EXCEPTION_PARSER = (ex, logger) -> {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            try (final PrintWriter writer = new PrintWriter(bos)) {
                ex.printStackTrace(writer);
            }
            final String string = bos.toString();
            final String[] split = pattern.split(string);
            logger.plain(split);
            ex.printStackTrace();
            return logger;
        };
    }

    private static Component wrapColor(Component component, TextColor color) {
        return Component.empty().color(color).append(component);
    }

    public static PluginFormatBuilder pluginFormatBuilder() {
        return new PluginFormatBuilder();
    }

    public static @NotNull Map<@NotNull Level, @NotNull LogMessageFormatter> empty() {
        return EMPTY;
    }

    public static @NotNull Map<@NotNull Level, @NotNull LogMessageFormatter> defaults() {
        return DEFAULT_FORMATTERS;
    }

    public static @NotNull ExceptionParser defaultExceptionParser() {
        return DEFAULT_EXCEPTION_PARSER;
    }

    public interface Builder {

        @NotNull Builder info(@NotNull LogMessageFormatter formatter);

        @NotNull Builder warning(@NotNull LogMessageFormatter formatter);

        @NotNull Builder error(@NotNull LogMessageFormatter formatter);

        @NotNull Builder debug(@NotNull LogMessageFormatter formatter);

    }

    public static class PluginFormatBuilder implements Builder {

        private final Map<Level, LogMessageFormatter> formatterMap = new HashMap<>(EMPTY);

        @Override
        public @NotNull Builder info(@NotNull LogMessageFormatter formatter) {
            formatterMap.put(Level.INFO, formatter);
            return this;
        }

        @Override
        public @NotNull Builder warning(@NotNull LogMessageFormatter formatter) {
            formatterMap.put(Level.WARNING, formatter);
            return this;
        }

        @Override
        public @NotNull Builder error(@NotNull LogMessageFormatter formatter) {
            formatterMap.put(Level.SEVERE, formatter);
            return this;
        }

        @Override
        public @NotNull Builder debug(@NotNull LogMessageFormatter formatter) {
            formatterMap.put(Level.FINE, formatter);
            return this;
        }

        public @NotNull Map<@NotNull Level, @NotNull LogMessageFormatter> build() {
            formatterMap.values().removeIf(Objects::isNull);
            return new HashMap<>(formatterMap);
        }
    }

}
