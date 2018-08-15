package io.costax.item13.entity;

import java.util.Objects;

public class PhoneNumber implements Cloneable {

    private  short areaCode;
    private  short prefix;
    private  short lineNum;

    private PhoneNumber (short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    public static PhoneNumber from (short areaCode, short prefix, short lineNum) {
        return new PhoneNumber(areaCode, prefix, lineNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return areaCode == that.areaCode &&
                prefix == that.prefix &&
                lineNum == that.lineNum;
    }

    @Override
    public int hashCode() {
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }

    /*
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    */

    @Override public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }



}
