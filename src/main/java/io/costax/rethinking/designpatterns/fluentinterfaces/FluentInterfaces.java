package io.costax.rethinking.designpatterns.fluentinterfaces;

import java.util.function.Consumer;

public class FluentInterfaces {

    public static void main(String[] args) {
        Mailer.send(mailer ->

                mailer.from("costax@costax.io")
                        .to("jlmc@costax.io")
                        .subject("your code sucks")
                        .body("...details...")

        );

    }
}

class Mailer {

    private Mailer() {
    }

    public static void send(Consumer<Mailer> block) {
        Mailer mailer = new Mailer();

        block.accept(mailer);

        System.out.println(".... sending ...");
    }

    public Mailer from(String addr) {
        System.out.println("from " + addr);
        return this;
    }

    public Mailer to(String to) {
        System.out.println("to " + to);
        return this;
    }

    public Mailer subject(String subject) {
        System.out.println("subject " + subject);
        return this;
    }

    public Mailer body(String body) {
        System.out.println("body " + body);
        return this;
    }
}
