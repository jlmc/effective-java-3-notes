package io.costax.rethinking.designpatterns.delegating;

import java.util.function.Supplier;

public class Lazy<T> {

    private final Supplier<T> supplier;
    private T instance;

    public Lazy(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T evaluate() {
        if (instance == null) {
            instance = supplier.get();
        }

        return instance;
    }
}
