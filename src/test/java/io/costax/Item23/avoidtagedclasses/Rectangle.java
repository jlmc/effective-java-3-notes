package io.costax.Item23.avoidtagedclasses;

public class Rectangle extends Figure {
    private double length;
    private double width;

    public Rectangle(final double length, final double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}
