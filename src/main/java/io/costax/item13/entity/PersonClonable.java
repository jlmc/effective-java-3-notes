package io.costax.item13.entity;

public class PersonClonable implements Cloneable {

    public String name;

    private PersonClonable(String name) {
        this.name = name;
    }

    public PersonClonable() {
        System.out.println("Person -- default constructor");
    }

    public static PersonClonable of(String name) {
        return new PersonClonable(name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
