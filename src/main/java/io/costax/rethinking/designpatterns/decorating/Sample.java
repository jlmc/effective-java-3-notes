package io.costax.rethinking.designpatterns.decorating;

import java.util.function.Function;

public class Sample {

    public static void main(String[] args) {
        Function<Integer, Integer> inc = e -> e + 1;

        printIt(5, "incremented", inc);
        printIt(10, "incremented", inc);


        Function<Integer, Integer> doubled = e -> e * 2;

        printIt(5, "double", doubled);
        printIt(10, "double", doubled);


        Function<Integer, Integer> incAndDoubled = inc.andThen(doubled);

        printIt(5, "incremented and doubled", incAndDoubled);
        printIt(10, "double", incAndDoubled);
    }

    private static void printIt(final int number, final String message, final Function<Integer, Integer> func) {
        System.out.println(number + " " + message + " : " + func.apply(number));
    }
}
