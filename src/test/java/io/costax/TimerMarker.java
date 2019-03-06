package io.costax;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.time.Duration;
import java.time.Instant;

public class TimerMarker implements TestRule {

    private TimerMarker() {
    }

    public static TimerMarker timer() {
        return new TimerMarker();
    }

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
                System.out.println("====>" + methodName + ": " + between);
            }
        };
    }
}