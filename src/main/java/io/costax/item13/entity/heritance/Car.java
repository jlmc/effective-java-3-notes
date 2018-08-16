package io.costax.item13.entity.heritance;

import java.util.Objects;

public class Car extends Vehicle implements Cloneable {

    private String id;
    private Owner owner;

    public Car(int type, String id, Owner owner) {
        super(type);
        this.id = id;
        this.owner = owner;
    }

    @Override
    public Car clone() {
        try {
            final Car clone = (Car) super.clone();
            clone.owner = (Owner) owner.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Owner getOwner() {
        return owner;
    }
}
