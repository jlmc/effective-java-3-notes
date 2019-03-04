package io.costax.revisiting;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class PhoneNumberComparableTest {

    @Test
    public void shouldSortPhoneNumber() {

        PhoneNumber p1 = PhoneNumber.of(123, 1234);
        PhoneNumber p2 = PhoneNumber.of(124, 1234);
        PhoneNumber p3 = PhoneNumber.of(124, 1235);
        PhoneNumber p4 = PhoneNumber.of(125, 1234);

        //final List<PhoneNumber> p11 = List.of(p1, p2, p3, p4);

        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<>(4);
        phoneNumbers.add(p4);
        phoneNumbers.add(p2);
        phoneNumbers.add(p1);
        phoneNumbers.add(p3);

        Collections.sort(phoneNumbers);

        Assert.assertEquals(phoneNumbers.get(0), p1);
        Assert.assertEquals(phoneNumbers.get(1), p2);
        Assert.assertEquals(phoneNumbers.get(2), p3);
        Assert.assertEquals(phoneNumbers.get(3), p4);

        phoneNumbers.sort(PhoneNumber.NUMBER_COMPARATOR);

        Assert.assertEquals(phoneNumbers.get(3), p3);

    }
}