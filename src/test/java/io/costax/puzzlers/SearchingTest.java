package io.costax.puzzlers;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchingTest {
        //  * Cache to support the object identity semantics of autoboxing for values between
        //     * -128 and 127 (inclusive) as required by JLS.

    @Test
    public void operatorsInBoxed() {


        final Integer x = Integer.valueOf("127");
        final Integer y = Integer.valueOf("127");
        System.out.println(x == y);

        final Integer a = Integer.valueOf("128");
        final Integer b = Integer.valueOf("128");
        System.out.println(a == b);

        Double d1 = Double.valueOf(1.0D);
        Double d2 = Double.valueOf(1.0D);
        System.out.println(d1 == d2);

    }
}