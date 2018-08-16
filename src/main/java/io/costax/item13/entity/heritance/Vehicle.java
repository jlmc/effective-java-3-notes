package io.costax.item13.entity.heritance;

public class Vehicle {

    public int type;

    public Vehicle(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        throw new CloneNotSupportedException(" --- Vehicle are not cloneable");
//    }
}
