package com.github.md5sha256.spigotutils.test;

import com.github.md5sha256.spigotutils.timing.GuavaAdapter;
import com.github.md5sha256.spigotutils.timing.IStopwatch;
import com.github.md5sha256.spigotutils.timing.Stopwatches;
import com.github.md5sha256.spigotutils.timing.VariableStopwatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestStopwatches {

    private static void testStopwatch(IStopwatch stopwatch) {
        stopwatch.reset();
        Assertions.assertFalse(stopwatch.isRunning());
        stopwatch.start();
        Assertions.assertTrue(stopwatch.isRunning());
        stopwatch.stop();
        Assertions.assertFalse(stopwatch.isRunning());
        stopwatch.reset().start();
        final long start = System.currentTimeMillis();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Assertions.fail(ex);
            return;
        }
        stopwatch.stop();
        final long end = System.currentTimeMillis();
        Assertions.assertEquals(end - start, stopwatch.elapsedMillis(), 10);
        stopwatch.reset();
        Assertions.assertEquals(0, stopwatch.elapsedMillis());
        Assertions.assertFalse(stopwatch.isRunning());
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Assertions.fail(ex);
        }
    }

    private static void testVariableStopwatch(VariableStopwatch stopwatch) {
        testStopwatch(stopwatch);
        final long newNanos = 1000;
        stopwatch.reset().setElapsedTime(newNanos, TimeUnit.NANOSECONDS);
        Assertions.assertEquals(stopwatch.elapsedNanos(), newNanos);
    }


    @Test
    @Order(0)
    public void testGuavaStopwatchAdapter() {
        testStopwatch( GuavaAdapter.ofUnstarted());
    }

    @Test
    @Order(1)
    public void testStopwatchWrappers() {
        Assertions.assertTrue(GuavaAdapter.ofStarted().isRunning());
        Assertions.assertFalse(GuavaAdapter.ofUnstarted().isRunning());
        testStopwatch(Stopwatches.synchronizedStopwatch(GuavaAdapter.ofUnstarted()));
        testVariableStopwatch(Stopwatches.variableStopwatch(GuavaAdapter.ofUnstarted()));
        testVariableStopwatch(Stopwatches.synchronizedVariableStopwatch(GuavaAdapter.ofUnstarted()));
    }

}
