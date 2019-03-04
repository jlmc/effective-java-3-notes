package io.costax.revisiting.functional;

import org.junit.Test;

import java.util.stream.Stream;


public class StreamsTest {

    @Test
    public void usingUnaryOperator() {
        // using UnaryOperator:

        /*
        // create a infinity stream of integers
        Stream.iterate(0, new UnaryOperator<Integer> () {
            @Override
            public Integer apply(final Integer integer) {
                return integer + 1;
            }
        });
        */

        Stream.iterate(0, integer -> integer + 1)
                .takeWhile(integer -> integer < 20)
                .forEach(integer -> System.out.printf("UnaryOperator: %d \n", integer));
    }

    @Test
    public void usingBinaryOperator() {
        /*
        // two parameters and the return with the same type
        final Optional<Integer> reduce = Stream.of(1, 2, 3, 4)
                .reduce(new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(final Integer x, final Integer y) {
                        return x + y;
                    }
                });
       */

        final Integer integer = Stream.of(1, 2, 3, 4)
                .reduce((x, y) -> x + y)
                .orElse(0);
        System.out.println(integer);
    }

    @Test
    public void usingPredicate() {
        Stream.of(Streams.STRINGS)
                .filter(str -> str.matches("\\d+"))
                .map(Integer::parseInt)
                .forEach(System.out::println);
    }

    @Test(expected = IllegalStateException.class)
    public void usingSupplier() {
        /*
        final Integer integer = Stream.<Integer>empty()
                .reduce((x, y) -> x + y)
                .orElseGet(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        return 9999;
                    }
                });
        */

        final Integer integer = Stream.<Integer>empty()
                .reduce((x, y) -> x + y)
                //.orElseGet(() -> 9999 * 1000);
                .orElseThrow(() -> new IllegalStateException("The source is empty"));

        System.out.println(integer);
    }

}