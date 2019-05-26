package io.costax.rethinking.designpatterns.aroundmethod;

import java.util.function.Consumer;

public class AroundMethod {

    public static void main(String[] args) {

        // how the programmers will use the resource class

        Resource.use(resource -> {

            resource.op1();
            resource.op2();

        });
    }

}

/**
 * we want to clean up the object quite deterministically as soon as we're done with it.
 * Java 7 ARM is a step closer to this, but still requires programmers to remember to use
 * the try format.
 * Using EAM pattern the Java 8 compiler can gently force the programmer to naturally use it without having
 * to remember.
 */
class Resource {

    private Resource() {
        System.out.println("Instance created");
    }

    public static void use(Consumer<Resource> consume) {
        Resource resource = new Resource();
        try {
            consume.accept(resource);
        } finally {
            resource.close();
        }
    }

    public void op1() {
        System.out.println("op1 called....");
    }

    public void op2() {
        System.out.println("op2 called...");
    }

    private void close() {
        System.out.println("do any cleanup here...");
    }

}
