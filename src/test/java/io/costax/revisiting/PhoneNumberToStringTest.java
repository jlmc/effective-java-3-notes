package io.costax.revisiting;

import org.junit.Assert;
import org.junit.Test;

public class PhoneNumberToStringTest {

    @Test
    public void shouldShowImportantDebugInformations() {
        PhoneNumber x = PhoneNumber.of(123, 1234);
        System.out.println(x);
    }

    @Test
    public void shouldFormattableToDisplayInformation() {
        PhoneNumber x = PhoneNumber.of(123, 1234);

        System.out.println(String.format("%s", x));
        System.out.printf("%s\n", x);

        Assert.assertEquals("123-1234", String.format("%s", x));
    }
}
