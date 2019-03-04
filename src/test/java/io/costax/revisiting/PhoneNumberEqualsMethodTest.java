package io.costax.revisiting;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhoneNumberEqualsMethodTest {

    @Test
    public void shouldBeReflexive() {
        // every objects must be equals to him self

        PhoneNumber p1 = PhoneNumber.of(123, 1234);

        assertTrue(p1.equals(p1));
    }

    @Test
    public void shouldBeSymmetrical() {
        // if X is equals to Y. then Y must be equals to X

        PhoneNumber x = PhoneNumber.of(999, 1234);
        PhoneNumber y = PhoneNumber.of(999, 1234);

        assertTrue(x.equals(y));
        assertTrue(y.equals(x));
    }

    @Test
    public void shouldBeTransitive() {
        // if X is equals to Y and Y is equals to Y, Then X must be equals to Z

        PhoneNumber x = PhoneNumber.of(999, 1234);
        PhoneNumber y = PhoneNumber.of(999, 1234);
        PhoneNumber z = PhoneNumber.of(999, 1234);

        assertTrue(x.equals(y));
        assertTrue(y.equals(z));
        assertTrue(x.equals(z));
    }

    @Test
    public void shouldBeConsistent() {
        // if we call the equals methods multiple time (for the same object values) the results should be always the same.

        PhoneNumber x = PhoneNumber.of(999, 1234);
        PhoneNumber y = PhoneNumber.of(999, 1234);

        assertTrue(x.equals(y));
        assertTrue(x.equals(y));
        assertTrue(x.equals(y));
        assertTrue(x.equals(y));
        assertTrue(x.equals(y));
        assertTrue(x.equals(y));
    }

    @Test
    public void shoudlBeNonNullity() {
        PhoneNumber x = PhoneNumber.of(999, 1234);

        assertFalse(x.equals(null));
    }
}