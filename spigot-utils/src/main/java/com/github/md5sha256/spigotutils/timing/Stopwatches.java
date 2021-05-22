package com.github.md5sha256.spigotutils.timing;

import com.github.md5sha256.spigotutils.serial.SerializableStopwatch;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Stopwatches {

    private Stopwatches() {
        throw new IllegalStateException("Utility classes cannot be instantiated");
    }

    public static @NotNull IStopwatch newInstance() {
        return GuavaAdapter.ofUnstarted();
    }

    public static @NotNull VariableStopwatch variableStopwatch(@NotNull IStopwatch stopwatch) {
        return new VariableStopwatchWrapper(stopwatch);
    }

    public static @NotNull IStopwatch synchronizedStopwatch(@NotNull IStopwatch stopwatch) {
        return new ConcurrentStopwatchWrapper(stopwatch);
    }

    public static @NotNull VariableStopwatch synchronizedVariableStopwatch(@NotNull IStopwatch stopwatch) {
        return new ConcurrentVariableStopwatchWrapper(stopwatch);
    }

    public static SerializableStopwatch ofBukkitSerializable(@NotNull IStopwatch stopwatch) {
        return new SerializableStopwatch(stopwatch.elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    private static class VariableStopwatchWrapper implements VariableStopwatch {

        private final IStopwatch stopwatch;
        private long elapsedNanos;

        protected VariableStopwatchWrapper(@NotNull IStopwatch stopwatch) {
            this.stopwatch = stopwatch;
        }

        @Override
        public boolean isRunning() {
            return this.stopwatch.isRunning();
        }

        @Override
        public long elapsed(@NotNull final TimeUnit timeUnit) {
            return timeUnit.convert(this.elapsedNanos + this.stopwatch.elapsedNanos(), TimeUnit.NANOSECONDS);
        }

        @Override
        public @NotNull VariableStopwatch start() {
            this.stopwatch.start();
            return this;
        }

        @Override
        public @NotNull VariableStopwatch stop() {
            this.stopwatch.stop();
            return this;
        }

        @Override
        public @NotNull VariableStopwatch reset() {
            this.stopwatch.reset();
            this.elapsedNanos = 0;
            return this;
        }

        @Override
        public @NotNull VariableStopwatch setElapsedTime(final long elapsed, @NotNull final TimeUnit timeUnit) {
            if (this.stopwatch.isRunning()) {
                this.stopwatch.reset().start();
            } else {
                this.stopwatch.reset();
            }
            this.elapsedNanos = timeUnit.toNanos(elapsed);
            return this;
        }

    }

    private static class ConcurrentStopwatchWrapper implements IStopwatch {

        protected final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final IStopwatch stopwatch;

        public ConcurrentStopwatchWrapper(@NotNull IStopwatch stopwatch) {
            this.stopwatch = stopwatch;
        }

        @Override
        public @NotNull IStopwatch start() {
            this.lock.writeLock().lock();
            try {
                this.stopwatch.start();
            } finally {
                this.lock.writeLock().unlock();
            }
            return this;
        }

        @Override
        public @NotNull IStopwatch stop() {
            this.lock.writeLock().lock();
            try {
                this.stopwatch.stop();
            } finally {
                this.lock.writeLock().unlock();
            }
            return this;
        }

        @Override
        public @NotNull IStopwatch reset() {
            this.lock.writeLock().lock();
            try {
                this.stopwatch.reset();
            } finally {
                this.lock.writeLock().unlock();
            }
            return this;
        }

        @Override
        public boolean isRunning() {
            this.lock.readLock().lock();
            try {
                return this.stopwatch.isRunning();
            } finally {
                this.lock.readLock().unlock();
            }
        }

        @Override
        public long elapsed(@NotNull final TimeUnit timeUnit) {
            return timeUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
        }

        @Override
        public long elapsedNanos() {
            this.lock.readLock().lock();
            try {
                return this.stopwatch.elapsedNanos();
            } finally {
                this.lock.readLock().unlock();
            }
        }

    }

    private static final class ConcurrentVariableStopwatchWrapper extends ConcurrentStopwatchWrapper implements
            VariableStopwatch {

        private final AtomicLong elapsedNanos = new AtomicLong();

        public ConcurrentVariableStopwatchWrapper(@NotNull IStopwatch stopwatch) {
            super(stopwatch);
        }

        @Override
        public @NotNull VariableStopwatch start() {
            super.start();
            return this;
        }

        @Override
        public @NotNull VariableStopwatch stop() {
            super.stop();
            return this;
        }

        @Override
        public @NotNull VariableStopwatch reset() {
            super.reset();
            return this;
        }

        @Override
        public @NotNull VariableStopwatch setElapsedTime(final long elapsed, @NotNull final TimeUnit timeUnit) {
            long nanos = timeUnit.toNanos(elapsed);
            super.reset();
            this.elapsedNanos.set(nanos);
            return this;
        }

        @Override
        public long elapsed(@NotNull final TimeUnit timeUnit) {
            return timeUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
        }

        @Override
        public long elapsedNanos() {
            return super.elapsedNanos() + this.elapsedNanos.get();
        }

    }

}
