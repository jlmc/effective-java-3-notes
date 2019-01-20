package io.costax.item13.entity.heritance;

import java.util.Objects;

public class Owner implements Cloneable {

    private final String id;
    private final String name;

    public Owner(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Owner of(String id, String name) {
        return new Owner(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return Objects.equals(id, owner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public Owner clone() {
        try {
            return (Owner) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Owner cloning error");
        }
    }
}
