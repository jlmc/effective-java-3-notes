package io.costax.item13.entity;

public class PersonNoClonable {

    public String name;

    private PersonNoClonable(String name) {
        this.name = name;
    }

    public PersonNoClonable() {
        System.out.println("Person -- default constructor");
    }

    public static PersonNoClonable of(String name) {
        return new PersonNoClonable(name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
