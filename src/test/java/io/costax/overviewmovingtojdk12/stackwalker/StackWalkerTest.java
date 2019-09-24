package io.costax.overviewmovingtojdk12.stackwalker;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
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
    void with_jdk11() {

        final List<Class<?>> interestingClasses = List.of(String.class, StackWalkerTest.class);

        // 1. To find the first caller filtering a known list of implementation class:
        final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        final Optional<? extends Class<?>> walk = stackWalker.walk(s ->

                s.map(StackWalker.StackFrame::getDeclaringClass)
                        .filter(clazz -> interestingClasses.contains(clazz))
                        .findFirst()
        );

        System.out.println(walk.map(s -> s.getName()).orElseGet(() -> "-- nothing --"));

        System.out.println("+++++++++");

        //2. To snapshot the top 10 stack frames of the current thread,
        List<StackWalker.StackFrame> stack = StackWalker.getInstance()
                .walk(s -> s.limit(10)
                        .collect(Collectors.toList()));

        stack.forEach(System.out::println);


        //final Collector<Object, ?, List<Object>> walk = StackWalker.getInstance().walk(s -> Collectors.toList());




    }
}
