package com.github.md5sha256.spigotutils.test;

import com.github.md5sha256.spigotutils.Common;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

public class TestCommonUtils {

    @Test
    public void testUtilityConstructor() {
        final Constructor<Common> constructor;
        try {
            constructor = Common.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            Assertions.fail(ex);
            return;
        }
        Assertions.assertThrows(
                ReflectiveOperationException.class,
                constructor::newInstance,
                "Cannot instantiate utility class"
        );
    }

    @Test
    @DisplayName("Test unsafe casting")
    public void testCasting() {
        final TimeUnit timeUnit = TimeUnit.DAYS;
        final Enum<?> anEnum = timeUnit;
        try {
            final TimeUnit casted = Common.unsafeCast(anEnum);
        } catch (ClassCastException ex) {
            Assertions.fail(ex);
        }
        Assertions.assertThrows(ClassCastException.class, () -> {
            String unsafe = Common.unsafeCast(anEnum);
        });
    }

    @Test
    @DisplayName("Test string utilities")
    public void testStringUtils() {
        final String raw = "apples on a tree.";
        final String titleCase = "Apples On A Tree.";
        final String capitalized = "Apples on a tree.";

        Assertions.assertEquals(titleCase, Common.titleCase(raw, " "));
        Assertions.assertEquals(capitalized, Common.capitalise(raw));
    }

    @Test
    @DisplayName("Test string replacing")
    public void testStringFormat() {
        final String placeholder = "%placeholder%";
        final String toReplace = "john";
        final String raw = String.format("abc %s def", placeholder);
        final String expected = String.format("abc %s def", toReplace);
        Assertions.assertEquals(Common.format(raw, placeholder, toReplace), expected);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Common.format(raw, placeholder, toReplace, "dummy"));
    }

    @Test
    @DisplayName("Test tick conversion")
    public void testTicks() {
        final long ticks = 10;
        final long expectedMillis = 500;
        Assertions.assertEquals(expectedMillis, Common.fromTicks(ticks, TimeUnit.MILLISECONDS));
        Assertions.assertEquals(ticks, Common.toTicks(500, TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("Test chat utils")
    public void testChatUtils() {
        final String ampersandText = "&bHello";
        Assertions.assertTrue(Common.isLegacyText(ampersandText));
        Assertions.assertFalse(Common.isLegacyText("Hello"));
        final Component component = Component.text("Hello").color(NamedTextColor.AQUA);
        final String legacyText = Common.legacyColorise(ampersandText);
        final Component legacyComponent = LegacyComponentSerializer.legacySection().deserialize(legacyText);
        Assertions.assertEquals(component, legacyComponent);

        final String hexText = "#55ffffHello";
        Assertions.assertTrue(Common.isHexText(hexText));
    }

}
