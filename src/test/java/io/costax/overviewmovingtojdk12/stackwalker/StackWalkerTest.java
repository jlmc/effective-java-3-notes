package io.costax.overviewmovingtojdk12.stackwalker;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class StackWalkerTest {

    @Test
    void before_jdk_9() {

        /*
         this works but requires eagle capture all the stack,
         all the result is a String
         there is no guaranty that all the stacktrace is retrieved
         */

        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        System.out.println(stackTrace);

        // alternatively

        final StackTraceElement[] stackTrace1 = Thread.currentThread().getStackTrace();
    }

    @Test
    void with_jdk() {
        final Collector<Object, ?, List<Object>> walk = StackWalker.getInstance().walk(s -> Collectors.toList());
    }
}
