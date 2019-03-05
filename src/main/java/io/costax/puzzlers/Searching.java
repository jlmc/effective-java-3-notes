package io.costax.puzzlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Searching {

    public static void main(String[] args) {
        String[] strings = {
                "0", "128", "2", "3", "4", "5"
        };

        // Translate String array into List of Integer
        List<Integer> integers = new ArrayList<>();
        for (String string : strings) {
            integers.add(Integer.valueOf(string));
        }

        System.out.println(Collections.binarySearch(integers, 128, cmp));
    }


    static Comparator<Integer> cmp = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i, final Integer j) {

            // the auto boxin
            return i < j ? -1 : (i == j ? 0 : 1);
        }
    };

    /*
    static Comparator<Integer> cmp = new Comparator<Integer>() {
        @Override
        public int compare(final Integer iBoxed, final Integer jBoxed) {
            final int i = iBoxed;
            final int j = jBoxed;
            // the auto boxin
            return Integer.compare(i, j);
        }
    };
    */



}
