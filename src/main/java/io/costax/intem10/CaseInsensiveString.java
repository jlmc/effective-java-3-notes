package io.costax.intem10;

import java.util.*;

public class CaseInsensiveString {
    private final String s;

    private CaseInsensiveString(String s) {
        Objects.requireNonNull(s, "The string can not be null");
        this.s = s;
    }

    private static CaseInsensiveString of(String a) {
        return new CaseInsensiveString(a);
    }

    public static void main(String[] args) {

        List<CaseInsensiveString> works = new ArrayList<>(0);
        works.add(CaseInsensiveString.of("a"));
        works.add(new CaseInsensiveString("B"));
        works.add(new CaseInsensiveString("c"));

        System.out.println(works.contains(CaseInsensiveString.of("a"))); // true
        System.out.println(works.contains(CaseInsensiveString.of("A"))); // true
        System.out.println(works.size()); // 3

        // esta collection Set fará uso do method hashset
        Set<CaseInsensiveString> singles = new HashSet<>(0);
        singles.add(CaseInsensiveString.of("a"));
        singles.add(CaseInsensiveString.of("A"));
        singles.add(CaseInsensiveString.of("b"));


        System.out.println(singles.contains(CaseInsensiveString.of("a"))); //true
        System.out.println(singles.contains(CaseInsensiveString.of("A"))); // true
        System.out.println(singles.contains(CaseInsensiveString.of("B"))); // true
        System.out.println(singles.size()); // 2
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof CaseInsensiveString) {
            return s.equalsIgnoreCase(((CaseInsensiveString) o).s);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s.toLowerCase());
    }
}
