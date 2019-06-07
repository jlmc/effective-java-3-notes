package io.costax.rethinking.designpatterns.iterator;

import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

public class IteratorSample {

    public static void main(String[] args) {

        System.out.println(factorial(5));
    }


    public static int factorial(int number) {
        int product = 1;
        for(int i = 1; i <= number; i++) {
            product *= i;
        }
        return product;
    }


    public static int factorialFunctional(int number) {
        return IntStream.rangeClosed(1, number)
                .reduce(1, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(final int left, final int right) {
                        return left * right;
                    }
                });
    }

    /*

    public static int factorialFunctional(int number) {
        return IntStream.rangeClosed(1, number)
                .reduce(1, (product, index) -> product * index);
    }

    public static void main(String[] args) {
        System.out.println(factorial(5));
        System.out.println(factorialFunctional(5));
    }

     */
}
