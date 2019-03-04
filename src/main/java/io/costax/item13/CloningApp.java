package io.costax.item13;

import io.costax.item13.entity.PhoneNumber;
import io.costax.item13.entity.Stack;

public class CloningApp {

    public static void main(String[] args) {
        cloneWithArray();
    }

    public static void cloneWithArray() {
        final Stack x = new Stack();
        final PhoneNumber pn = PhoneNumber.from((short) 1, (short) 2, (short) 3);
        x.push(pn);

        final Stack clone = x.clone();

        System.out.println(clone != x); // true
        System.out.println(x.getClass() == clone.getClass()); // true
        System.out.println(x.equals(clone)); // true

        final Object pop = clone.pop();

        System.out.println(pop == pn);
    }

    public static void simpleClone() {

        final PhoneNumber x = PhoneNumber.from((short) 1, (short) 2, (short) 3);
        final Object clone = x.clone();

        System.out.println(clone != x); // true
        System.out.println(x.getClass() == clone.getClass()); // true
        System.out.println(x.equals(clone)); // true
    }
}
