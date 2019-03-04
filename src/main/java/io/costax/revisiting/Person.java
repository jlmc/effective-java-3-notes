package io.costax.revisiting;

import com.google.common.base.Preconditions;

public class Person {

    private final String name;
    private final String surname;
    private String title;
    private String prefix;

    private Person(Builder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.title = builder.title;
        this.prefix = builder.prefix;
    }

    public static Builder builder(final String name, final String surname) {
        return new Builder(name, surname);
    }

    public static final class Builder {
        private final String name;
        private final String surname;
        private String title;
        private String prefix;

        private Builder(final String name, final String surname) {
            this.name = name;
            this.surname = surname;
        }

//        public static Builder of(final String name, final String surname) {
//            return new Builder(name, surname);
//        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Person build() {
            Preconditions.checkState(title != null || prefix != null);
            return new Person(this);
        }
    }
}
