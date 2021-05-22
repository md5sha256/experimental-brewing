package com.github.md5sha256.spigotutils.test;

import com.github.md5sha256.spigotutils.sql.ArithmeticComparator;
import com.github.md5sha256.spigotutils.sql.DateQuery;
import com.github.md5sha256.spigotutils.sql.NumberQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class TestSqlUtils {

    @Test
    public void testArithmeticOperands() {
        Assertions.assertEquals(ArithmeticComparator.EQUAL, ArithmeticComparator.NOT_EQUAL.negate());
        Assertions.assertEquals(ArithmeticComparator.NOT_EQUAL, ArithmeticComparator.EQUAL.negate());
        Assertions.assertEquals(ArithmeticComparator.GREATER, ArithmeticComparator.LESS.negate());
        Assertions.assertEquals(ArithmeticComparator.LESS, ArithmeticComparator.GREATER.negate());
        Assertions.assertEquals(ArithmeticComparator.GREATER_OR_EQUAL, ArithmeticComparator.LESS_OR_EQUAL.negate());
        Assertions.assertEquals(ArithmeticComparator.LESS_OR_EQUAL, ArithmeticComparator.GREATER_OR_EQUAL.negate());
    }

    @Test
    public void testDateQuery() {

        final Constructor<DateQuery> constructor;
        try {
            constructor = DateQuery.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            Assertions.fail(ex);
            return;
        }
        Assertions.assertThrows(
                ReflectiveOperationException.class,
                constructor::newInstance,
                "No instances of the utility class are allowed."
        );

        final long start = 100;
        final NumberQuery afterQuery = DateQuery.after(start);
        final NumberQuery beforeQuery = DateQuery.before(100);
        final NumberQuery negatedAfter = afterQuery.negate();
        final NumberQuery negatedBefore = beforeQuery.negate();

        Assertions.assertEquals(afterQuery, negatedBefore);
        Assertions.assertEquals(beforeQuery, negatedAfter);

        Assertions.assertTrue(afterQuery.test(101));
        Assertions.assertTrue(negatedBefore.test(101));

        Assertions.assertFalse(afterQuery.test(100));
        Assertions.assertFalse(negatedBefore.test(100));

        Assertions.assertFalse(afterQuery.test(99));
        Assertions.assertFalse(negatedBefore.test(99));

        Assertions.assertTrue(beforeQuery.test(99));
        Assertions.assertTrue(negatedAfter.test(99));

        Assertions.assertFalse(beforeQuery.test(100));
        Assertions.assertFalse(negatedAfter.test(100));

        Assertions.assertFalse(beforeQuery.test(101));
        Assertions.assertFalse(negatedAfter.test(101));

        final NumberQuery atQuery = DateQuery.at(100);
        final NumberQuery negatedAtQuery = atQuery.negate();

        Assertions.assertTrue(atQuery.test(100));
        Assertions.assertFalse(negatedAtQuery.test(100));

        Assertions.assertFalse(atQuery.test(101));
        Assertions.assertTrue(negatedAtQuery.test(101));

        Assertions.assertFalse(atQuery.test(99));
        Assertions.assertTrue(negatedAtQuery.test(99));

        final NumberQuery betweenQuery = DateQuery.between(99, 101);
        final NumberQuery negatedBetween = betweenQuery.negate();

        Assertions.assertEquals(betweenQuery, negatedBetween.negate());

        Assertions.assertTrue(betweenQuery.test(100));
        Assertions.assertFalse(negatedBetween.test(100));

        Assertions.assertFalse(betweenQuery.test(99));
        Assertions.assertTrue(negatedBetween.test(99));

        Assertions.assertFalse(betweenQuery.test(101));
        Assertions.assertTrue(negatedBetween.test(101));

    }

}
