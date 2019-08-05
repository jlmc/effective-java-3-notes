package io.costax.Item23.avoidtagedclasses;

public class Circle extends Figure {
    private final double radius;

    public Circle(final double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * (radius * radius);
    }
}
