package io.costax.chapter8;

import org.junit.Assert;
import org.junit.Test;

public class UseVarargsJudiciously_53 {

    private static int min(int firstArg, int... remainingArgs) {
        int min = firstArg;
        for (int arg : remainingArgs) {
            if (arg < min) {
                min = arg;
            }
        }
        return min;
    }

    private static int sum(int... values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

    /*
    private int min(int... args) {
        // this allow us to invoke the method without parameters
        // the idea is to have at least one parameter

        if (args.length == 0) throw new IllegalArgumentException("Too few arguments");

        int min = args[0];
        for (int i = 1; i < args.length; i++) {
            if (args[i] < min) {
                min = args[i];
            }
        }

        return min;
    }
    */

    @Test
    public void sumExample() {
        final int sum = sum(1, 2, 3);

        Assert.assertEquals(6, sum);
    }

    @Test
    public void goodMethodWithAtLeatOneParameter() {
        final int min = min(123, 189, 51, 891);
        Assert.assertEquals(51, min);
    }


}
