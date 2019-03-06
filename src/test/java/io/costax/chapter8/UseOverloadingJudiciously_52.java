package io.costax.chapter8;

import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

public class UseOverloadingJudiciously_52 {

    // Broken! - What does this program print?
    private static class CollectionClassifier {

        public static String classify(Set<?> s) {
            return "Set";
        }

         static String classify(List<?> lst) {
            return "List";
        }
        static String classify(Collection<?> c) {
            return "Unknown Collection";
        }


    }

    @Test
    public void breakOverloading() {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        /*
         * The behavior of this program is counterintuitive because selection among overloaded methods is static,
         * while selection among overridden methods is dynamic.
         */
        for (Collection<?> c : collections)
            System.out.println(CollectionClassifier.classify(c));
    }
}
