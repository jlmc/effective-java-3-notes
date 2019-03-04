package io.costax.item13.entity;

import java.util.Objects;

public class PersonClonableNoCasts implements Cloneable {

    public String id;

    private PersonClonableNoCasts(String id) {
        this.id = id;
    }

    public PersonClonableNoCasts() {
        System.out.println("Person -- default constructor");
    }

    public static PersonClonableNoCasts of(String name) {
        return new PersonClonableNoCasts(name);
    }

    @Override
    public PersonClonableNoCasts clone() {
        try {
            return (PersonClonableNoCasts) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("can't happen");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonClonableNoCasts that = (PersonClonableNoCasts) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
