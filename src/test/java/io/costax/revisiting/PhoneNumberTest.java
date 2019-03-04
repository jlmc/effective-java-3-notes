package io.costax.revisiting;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class PhoneNumberTest {

    @Test
    public void usingCommonInstanceMultipleTimes() {
        PhoneNumber common = PhoneNumber.of(123, 1234);
        PhoneNumber common2 = PhoneNumber.of(123, 1234);

        assertSame(common, common2);
    }
}