package io.costax.puzzlers;

import org.junit.Test;

public class Oddity {

    public static boolean isOdd(int i) {
        //return i % 2 == 1;
        return i % 2 != 0;
    }

    @Test
    public void testOdity() {

        System.out.println(isOdd(1));
        System.out.println(isOdd(2));
        System.out.println(isOdd(3));
        System.out.println(isOdd(4));

        System.out.println("--");

        System.out.println(isOdd(-1));
        System.out.println(isOdd(-2));
        System.out.println(isOdd(-3));
        System.out.println(isOdd(-4));
    }
}
