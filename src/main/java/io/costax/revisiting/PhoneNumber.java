package io.costax.revisiting;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.Formattable;
import java.util.Formatter;

public class PhoneNumber implements Formattable, Comparable<PhoneNumber> {

    public static final Comparator<PhoneNumber> COMPARATOR = Comparator.comparingInt((PhoneNumber p) -> p.areaCode).thenComparing(p -> p.number);
    public static final Comparator<PhoneNumber> NUMBER_COMPARATOR = Comparator.comparingInt((PhoneNumber p) -> p.number);
    private static final PhoneNumber COMMUN = new PhoneNumber(123, 1234);
    private final int areaCode;
    private final int number;

    //private final int eagleHashCodeValueHashCodeValue;
    private int lazyHashCodeValue = 0;

    private PhoneNumber(final int areaCode, final int number) {
        this.areaCode = areaCode;
        this.number = number;

        //this.eagleHashCodeValue = Objects.hash(this.areaCode, this.number);
    }

    public static PhoneNumber of(final int areaCode, final int number) {
        Preconditions.checkArgument(areaCode > 100);
        Preconditions.checkArgument(number > 1000);

        if (areaCode == 123 && number == 1234) {
            return COMMUN;
        }

        return new PhoneNumber(areaCode, number);
    }

    public int getAreaCode() {
        return areaCode;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;

        final PhoneNumber that = (PhoneNumber) o;
        return areaCode == that.areaCode &&
                number == that.number;
    }

    @Override
    public int hashCode() {
        int result = lazyHashCodeValue;
        if (result == 0) {
            result = Integer.hashCode(areaCode);
            result = 31 * result + Integer.hashCode(number);
            //result = 31 * result + Integer.hashCode(number1);
            lazyHashCodeValue = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("areaCode", areaCode)
                .add("number", number)
                .toString();
    }

    @Override
    public void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        // Because we are exposing the value of the areaCode and Number, we should also provider the getters of areaCode and Number,
        // this way we give some more flexibility to the clients Apis
        // but atention for the immutability of the result values, special if we are using value objects
        formatter.format("%d-%d", areaCode, number);
    }

    @Override
    public int compareTo(final PhoneNumber o) {
        // with java 8 or after
        return COMPARATOR.compare(this, o);

        // before Java 8: using Guava
        //return ComparisonChain.start()
        //        .compare(this.areaCode, o.areaCode)
        //        .compare(this.number, o.number)
        //        .result();
    }
}
