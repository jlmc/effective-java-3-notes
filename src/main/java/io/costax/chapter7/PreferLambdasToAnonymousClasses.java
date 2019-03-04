package io.costax.chapter7;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * prefer lambdas to anonymous classes (42)
 */
public class PreferLambdasToAnonymousClasses {

    public static final String[] STRINGS = {
            "a", "7", "4", "z", "T", "c", "10", "h", "2", "123.abc"
    };



    public static void main(String[] args) {
        final List<String> words = Arrays.asList(STRINGS);


        // Historically, anonymous classes for function objects
        // Best practice as java 7
        /*
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(final String x, final String y) {
                return Integer.compare(x.length(), y.length());
            }
        });
        */

        // use a lambda
        // Better still - use comparator constructor method
        Collections.sort(words, Comparator.comparingInt(String::length));

        // even better - also sort method on the List
        words.sort(Comparator.comparingInt(String::length));
    }

}
