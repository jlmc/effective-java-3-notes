package io.costax.revisiting.functional;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Streams {

    public static final String[] STRINGS = {
            "a", "7", "4", "z", "T", "c", "10", "h", "2", "123.abc"
    };

    public static void main(String[] args) {

        final Optional<Integer> reduce = Stream.of(1, 2, 3, 4)
                .reduce(new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(final Integer x, final Integer y) {
                        return x + y;
                    }
                });
    }
}
