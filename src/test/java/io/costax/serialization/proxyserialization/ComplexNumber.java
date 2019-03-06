package io.costax.serialization.proxyserialization;

import java.io.Serializable;

public class ComplexNumber implements Serializable {

    private final double real;
    private final double imaginary;
    private final double magnitude;
    private final double angle;

    private ComplexNumber(final double real,
                          final double imaginary,
                          final double magnitude,
                          final double angle) {
        this.real = real;
        this.imaginary = imaginary;
        this.magnitude = magnitude;
        this.angle = angle;
    }

    private static ComplexNumber fromCoordinates(final double real, final double imaginary) {
        final double hypot = Math.hypot(real, imaginary);
        final double v = Math.atan2(imaginary, real);

        return of(real, imaginary, hypot, v);
    }

    public static ComplexNumber of(final double real,
                                   final double imaginary,
                                   final double magnitude,
                                   final double angle) {
        return new ComplexNumber(real, imaginary, magnitude, angle);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "ComplexNumber{" +
                "real=" + real +
                ", imaginary=" + imaginary +
                ", magnitude=" + magnitude +
                ", angle=" + angle +
                '}';
    }

    private Object writeReplace() {
        System.out.println("--> replacing the ComplexNumber to an ComplexNumberSerializationProxy before serialize...");

        return new ComplexNumberSerializationProxy(this);
    }

    private static class ComplexNumberSerializationProxy implements Serializable {
        private final double real;
        private final double imaginary;

        public ComplexNumberSerializationProxy(ComplexNumber complexNumber) {
            this.real = complexNumber.real;
            this.imaginary = complexNumber.imaginary;
        }

        /**
         * After the proxy is deserialized, it invokes a static factory method
         * to create a 'ComplexNumber' "the regular way".
         */
        private Object readResolve() {
            System.out.println("--> replacing the ComplexNumberSerializationProxy to an ComplexNumber after deserialize...");

            return ComplexNumber.fromCoordinates(real, imaginary);
        }
    }


}
