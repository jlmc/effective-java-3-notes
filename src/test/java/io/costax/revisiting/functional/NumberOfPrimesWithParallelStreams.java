package io.costax.revisiting.functional;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

public class NumberOfPrimesWithParallelStreams {

    private static final long N = 9999999L;

    @Rule
    public TimerMarker marker = new TimerMarker();

    @Test
    public void p1Sequential() {
        final long primes = countPrimesUntil(N);
        System.out.println("Result: " + primes);

        // Result: 664579
        // Time: p1Sequential: PT20.9709S
        Assert.assertThat(primes, CoreMatchers.is(664579L));
    }

    @Test
    public void p1Parallel() {
        final long primes = countPrimesUntilInParallel(N);
        System.out.println("Result: " + primes);

        // Result: 664579
        // Time: p1Parallel: PT7.944196S
        Assert.assertThat(primes, CoreMatchers.is(664579L));
    }

    private long countPrimesUntil(final long n) {
        return LongStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

    private long countPrimesUntilInParallel(final long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

    public static class TimerMarker implements TestRule {

        @Override
        public Statement apply(final Statement base, final Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final String methodName = description.getMethodName();

                    final Instant timeBefore = Instant.now();

                    base.evaluate();

                    final Instant timeAfter = Instant.now();
                    final Duration between = Duration.between(timeBefore, timeAfter);
                    System.out.println(methodName + ": " + between);
                }
            };
        }
    }
}
