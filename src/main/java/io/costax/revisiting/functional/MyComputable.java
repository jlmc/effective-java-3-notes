package io.costax.revisiting.functional;

@FunctionalInterface
public interface MyComputable {

    int apply(int x);

    default int dot(int x) {
        return x;
    }
}
