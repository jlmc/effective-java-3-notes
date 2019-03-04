package io.costax.revisiting.functional;

import org.junit.Test;

public class PuzzlerExplanation {

    @Test
    public void whatIsTheOutput() {
        "Hello world!!!".chars()
                .forEach(System.out::print);

        // Output: 7210110810811132119111114108100333333
    }

    @Test
    public void whatIsTheOutputNow() {
        // LOL: The chars() methods returns a IntStream
        "Hello world!!!".chars()
                .forEach(x -> System.out.print((char) x));

        // output: Hello world!!!
    }


}
