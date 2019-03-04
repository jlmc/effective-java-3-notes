package io.costax.revisiting.enums;

import java.io.Serializable;

public class SingletonBeforeJava5 implements Serializable {

    public static final SingletonBeforeJava5 INSTANCE = new SingletonBeforeJava5();

    private SingletonBeforeJava5() {
    }

    // implement readResolve method
    protected Object readResolve() {
        return INSTANCE;
    }
}
