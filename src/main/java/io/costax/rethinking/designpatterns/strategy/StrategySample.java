package io.costax.rethinking.designpatterns.strategy;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategySample {


    public static void main(String[] args) {
        final Supplier<List<Integer>> numbers = () -> IntStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toList());


        int totalValue = totalValues(numbers, (Integer i) -> true);

        Predicate<Integer> isEven = integer -> integer % 2 == 0;

        int totalEvenValue = totalValues(numbers, isEven);
        int totalOddValue = totalValues(numbers, isEven.negate());

        System.out.println("-- total Values: " + totalValue);
        System.out.println("-- total Even Values: " + totalEvenValue);
        System.out.println("-- total Odd Values: " + totalOddValue);
    }

    private static int totalValues(final Supplier<List<Integer>> numbers, Predicate<Integer> selector) {

        return numbers.get().stream()
                .filter(selector)
                .mapToInt(e -> e)
                .sum();

        /*
        final List<Integer> values = numbers.get();

        var total = 0;

        for (Integer e : values) {
            if (selector.test(e)) total += e;
        }

        return total;
        */
    }

    /*
    public static void main(String[] args) {
        final Supplier<List<Integer>> numbers = () -> IntStream.rangeClosed(1, 10)
                .mapToObj(i -> i)
                .collect(Collectors.toList());


        int totalValue = totalValuesInteractiveStyle(numbers);
        int totalEvenValue = totalEvenValuesInteractiveStyle(numbers);
        int totalOddValue = totalEvenValuesInteractiveStyle(numbers);
    }







    private static int totalValuesInteractiveStyle(final Supplier<List<Integer>> numbers) {
        final List<Integer> values = numbers.get();

        int total = 0;

        for (Integer e : values) {
            total += e;
        }

        return total;
    }

    private static int totalEvenValuesInteractiveStyle(final Supplier<List<Integer>> numbers) {
        final List<Integer> values = numbers.get();

        int total = 0;

        for (Integer e : values) {
            if (e % 2 == 0) {
                total += e;
            }
        }

        return total;
    }

    private static int totalEvenOddInteractiveStyle(final Supplier<List<Integer>> numbers) {
        final List<Integer> values = numbers.get();

        int total = 0;

        for (Integer e : values) {
            if (e % 2 != 0) {
                total += e;
            }
        }

        return total;
    }


     */

}
