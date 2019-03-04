package io.costax.revisiting.methodreferences;

import java.time.Instant;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * different types of methods references.
 */
public class MethodReferences {

    public static void main(String[] args) {
        //st(i -> Integer.parseInt(i));
        st(Integer::parseInt);

        //bound(i -> Instant.now().isAfter(i));
        bound(Instant.now()::isAfter);

        //unbound(s -> s.toLowerCase());
        unbound(String::toLowerCase);

        //constructor(() -> new TreeMap<>());
        constructor(TreeMap::new);

        //arrays(i -> new int[i]);
        arrays(int[]::new);
    }

    public static void st(Function<String, Integer> function) {
    }

    public static void bound(Predicate<Instant> predicate) {
    }

    public static void unbound(UnaryOperator<String> operator) {
    }

    public static void constructor(Supplier<TreeMap<String, String>> supplier) {
    }

    public static void arrays(Function<Integer, int[]> function) {
    }

}
