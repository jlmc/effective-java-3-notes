package io.costax.rethinking.designpatterns.delegating;

public class DelegatingSample {

    public static void main(String[] args) {
        int x = 14;

        // missing some x manipulation

        var temp = new Lazy<>(() -> compute(x));

        if (x > 5 && temp.evaluate() > 7) {
            System.out.println("Path 1");
        } else {
            System.out.println("Path 2");
        }


    }

    static int compute(int n) {

        System.out.println("called compute... " + n);

        return n * 2;
    }
}
