package com.github.md5sha256.spigotutils.logging;


import com.github.md5sha256.spigotutils.concurrent.TaskSynchronizer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LogDumpImpl implements LogDump {

    private final TaskSynchronizer taskSynchronizer;
    private final ILogger logger;
    private final Map<Component, LogLevel> toLog = Collections.synchronizedMap(new LinkedHashMap<>());

    public LogDumpImpl(final TaskSynchronizer synchronizer, final ILogger logger) {
        this.logger = logger;
        this.taskSynchronizer = synchronizer;
    }

    @Override
    public void plain(@NotNull String... messages) {
        for (String s : messages) {
            toLog.put(Component.text(s), LogLevel.ALL);
        }
    }

    @Override
    public void info(Component... messages) {
        for (Component c : messages) {
            toLog.put(c, LogLevel.INFO);
        }
    }

    @Override
    public void debug(@NotNull Component... messages) {
        for (Component c : messages) {
            toLog.put(c, LogLevel.DEBUG);
        }
    }

    @Override
    public void warn(@NotNull Component... messages) {
        for (Component c : messages) {
            toLog.put(c, LogLevel.WARN);
        }
    }

    @Override
    public void error(@NotNull Component... messages) {
        for (Component c : messages) {
            toLog.put(c, LogLevel.ERROR);
        }
    }

    @Override
    public @NotNull List<Component> accumulated() {
        return new ArrayList<>(toLog.keySet());
    }

    @Override
    public void dump() {
        final Map<Component, LogLevel> map = new LinkedHashMap<>(this.toLog);
        this.toLog.clear();
        if (!Bukkit.isPrimaryThread()) {
            taskSynchronizer.syncNow(this::dump);
            return;
        }
        for (Map.Entry<Component, LogLevel> entry : map.entrySet()) {
            final LogLevel level = entry.getValue();
            switch (level) {
                case INFO:
                    logger.info(entry.getKey());
                    break;
                case WARN:
                    logger.warn(entry.getKey());
                    break;
                case DEBUG:
                    logger.debug(entry.getKey());
                    break;
                case ERROR:
                    logger.error(entry.getKey());
                    break;
                case ALL:
                    logger.plain(entry.getKey());
                    break;
                default:
            }
        }
    }

    private enum LogLevel {
        DEBUG, INFO, WARN, ERROR, ALL
    }
}
