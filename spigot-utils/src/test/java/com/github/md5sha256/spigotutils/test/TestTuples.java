package com.github.md5sha256.spigotutils.test;

import com.github.md5sha256.spigotutils.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTuples {

    @Test
    public void testTuple() {

        Assertions.assertEquals(Pair.EMPTY, Pair.empty());

        final Pair<String, String> emptyPair = Pair.of(null, null);
        Assertions.assertNull(emptyPair.primary());
        Assertions.assertNull(emptyPair.secondary());
        Assertions.assertEquals("abc", emptyPair.primary("abc"));
        Assertions.assertEquals("abc", emptyPair.secondary("abc"));
        Assertions.assertEquals(0, emptyPair.hashCode());
        Assertions.assertEquals(Pair.empty(), emptyPair);

        final Pair<String, String> pair = Pair.of("abc", "def");
        Assertions.assertEquals(pair.primary(), "abc");
        Assertions.assertEquals(pair.secondary(), "def");

        Assertions.assertEquals(pair.primary(), pair.primary("ghk"));
        Assertions.assertEquals(pair.secondary(), pair.secondary("ghk"));

    }

}
