package io.costax.item17.minimizemutability;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

/**
 * 1. Don’t provide methods that modify the object’s state (known as
 * mutators).
 * <p>
 * 2. Ensure that the class can’t be extended. This prevents careless or
 * malicious subclasses from compromising the immutable behavior of the
 * class by behaving as if the object’s state has changed. Preventing subclassing
 * is generally accomplished by making the class final, but there is an
 * alternative that we’ll discuss later.
 * <p>
 * 3. Make all fields final. This clearly expresses your intent in a manner that is
 * enforced by the system. Also, it is necessary to ensure correct behavior if a
 * reference to a newly created instance is passed from one thread to another
 * without synchronization, as spelled out in the memory model [JLS, 17.5;
 * Goetz06, 16].
 * <p>
 * 4. Make all fields private. This prevents clients from obtaining access to
 * mutable objects referred to by fields and modifying these objects directly.
 * While it is technically permissible for immutable classes to have public final
 * fields containing primitive values or references to immutable objects, it is
 * not recommended because it precludes changing the internal representation
 * in a later release (Items 15 and 16).
 * <p>
 * 5. Ensure exclusive access to any mutable components. If your class has
 * any fields that refer to mutable objects, ensure that clients of the class cannot
 * obtain references to these objects. Never initialize such a field to a clientprovided object reference or return the field from an accessor. Make
 * defensive copies (Item 50) in constructors, accessors, and readObject
 * methods (Item 88)
 */
public final class ComplexNumber {

    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);
    public static final ComplexNumber I = new ComplexNumber(0, 1);

    private static final Set<ComplexNumber> CACHE_VALUES = Set.of(ZERO, ONE, I);

    private final double re;
    private final double im;

    private ComplexNumber(final double re, final double im) {
        this.re = re;
        this.im = im;
    }

    public static ComplexNumber valueOf(final double re, final double im) {
        return CACHE_VALUES.stream()
                .filter(c -> Double.compare(c.re, re) == 0 && Double.compare(c.im, im) == 0)
                .findFirst()
                .orElseGet(() -> new ComplexNumber(re, im));
    }

    public double realPart() {
        return re;
    }

    public double imaginaryPart() {
        return im;
    }

    public ComplexNumber plus(ComplexNumber c) {
        return new ComplexNumber(re + c.re, im + c.im);
    }

    public ComplexNumber minus(ComplexNumber c) {
        return new ComplexNumber(re - c.re, im - c.im);
    }

    public ComplexNumber times(ComplexNumber c) {
        return new ComplexNumber(
                re * c.re - im * c.im,
                re * c.im + im * c.re);
    }

    public ComplexNumber dividedBy(ComplexNumber c) {
        double tmp = c.re * c.re + c.im * c.im;

        return new ComplexNumber(
                (re * c.re + im * c.im) / tmp,
                (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ComplexNumber))
            return false;
        ComplexNumber c = (ComplexNumber) o;

        // See page 47 to find out why we use compare instead of == operator
        // - for primitive fields whose type is not float or double ==
        // - for object references fields call the equals method recursively
        // - for float (primitive) field use the static method Float.compare( float, float)
        // - for double (primitive) field use the static method Double.compare( double, double)
        // This special treatment of float and double fields is made necessary by the existence of
        // Float.NaN, -0.0f and the analogous double values, see JLS 15.21.1 or the documentation of Float.equals for details
        //
        // -While for Double and Float fields we could use the static method Float.equals or Double.equals
        // this would entail autoboxing on every comparision , which would have poor performance
        //
        // Some Objects references fields may legitimately contain null. To Avoid the possibility of NullPointerException,
        // check such fields for equality using the static method Objects.equals (Object, Object)

        // For better performance of the equals method we should first compare fields that:
        //  -- more likely to differ,
        //  -- less expensive  to compare
        return Double.compare(c.re, re) == 0
                && Double.compare(c.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
