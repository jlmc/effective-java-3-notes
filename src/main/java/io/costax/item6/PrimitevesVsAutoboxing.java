package io.costax.item6;

import java.time.Duration;
import java.time.Instant;

public class PrimitevesVsAutoboxing {

    /**
     * Prefer primitives to boxed primitives, and watch out for unintentional autoboxing.
     */

    public static void main(String[] args) {
        final Instant startup = Instant.now();

       long sum = getSumLongWithoutAutoBoxing();  // The duration is: PT0.614S
        //Long sum = getSumLongWithAutoBoxing();  // The duration is: PT7.866S

        final Duration between = Duration.between(startup, Instant.now());

        System.out.println("The duration is: " + between);
        System.out.println(sum);
    }

    private static Long getSumLongWithoutAutoBoxing() {
        long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++)
            sum += i;
        return sum;
    }

    private static Long getSumLongWithAutoBoxing() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++)
            sum += i;
        return sum;
    }
}
