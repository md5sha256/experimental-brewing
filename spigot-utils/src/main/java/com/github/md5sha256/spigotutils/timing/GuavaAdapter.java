package com.github.md5sha256.spigotutils.timing;

import com.google.common.base.Stopwatch;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class GuavaAdapter {

    public static @NotNull IStopwatch ofUnstarted() {
        return new GuavaWrapper(Stopwatch.createUnstarted());
    }

    public static @NotNull IStopwatch ofStarted() {
        return new GuavaWrapper(Stopwatch.createStarted());
    }

    public static @NotNull IStopwatch ofStopwatch(@NotNull Stopwatch stopwatch) {
        return new GuavaWrapper(stopwatch);
    }

    private static class GuavaWrapper implements IStopwatch {

        private final Stopwatch stopwatch;

        private GuavaWrapper(@NotNull Stopwatch stopwatch) {
            this.stopwatch = Objects.requireNonNull(stopwatch);
        }

        private GuavaWrapper() {
            this(Stopwatch.createUnstarted());
        }

        @Override
        public @NotNull IStopwatch start() {
            if (!this.stopwatch.isRunning()) {
                this.stopwatch.start();
            }
            return this;
        }

        @Override
        public @NotNull IStopwatch stop() {
            if (this.stopwatch.isRunning()) {
                this.stopwatch.stop();
            }
            return this;
        }

        @Override
        public @NotNull IStopwatch reset() {
            this.stopwatch.reset();
            return this;
        }

        @Override
        public long elapsed(@NotNull final TimeUnit timeUnit) {
            return this.stopwatch.elapsed(timeUnit);
        }

        @Override
        public boolean isRunning() {
            return this.stopwatch.isRunning();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }

            GuavaWrapper that = (GuavaWrapper) o;

            return this.stopwatch.equals(that.stopwatch);
        }

        @Override
        public int hashCode() {
            return this.stopwatch.hashCode();
        }

    }

}
