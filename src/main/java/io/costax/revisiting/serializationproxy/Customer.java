package io.costax.revisiting.serializationproxy;

import java.io.Serializable;
import java.util.Optional;

/**
 * The best way to avoid problems is to not use optional if the class is serializable.
 * But if really we must do it it, here we have an example of how to do it
 */
public class Customer implements Serializable {

    // this can be null
    private String title;

    //private Optional<String> title;

    public static void main(String[] args) {
        final Optional<String> title = new Customer().getTitle();
        //title.orElseThrow(IllegalStateException::new);
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }
}
