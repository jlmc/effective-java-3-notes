package io.costax.chapter7;

import java.util.HashMap;
import java.util.Map;

public class PreferMethodsReferencesToLambdas_43 {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);


        // lambdas are succinct
        map.merge("a", 4, (count, incr) -> count + incr);

        // but method reference can be more so
        map.merge("a", 4, Integer::sum);


        System.out.println(map);
    }
}
