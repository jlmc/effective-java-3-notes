package io.costax.datetime.legacy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.TimeZone;

class DateTest {

    private static final long TIMESTAMP = 1569448885943L;

    private TimeZone defaultTimeZone;

    @BeforeEach
    void setUp() {
        this.defaultTimeZone = TimeZone.getDefault();
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    void createASpecificDateInstance() {
        final int day = 10;
        final int month = 2; // fevereiro
        final int year = 2000;

        // using the deprecated constructor
        final Date date = new Date(year - 1900, month - 1, day);

        System.out.println(date);
    }

    @Test
    void dateTest() {
        final Date date = new Date(TIMESTAMP);

        //System.out.println(TimeZone.getDefault());

        // be careful to use the method 'TimeZone.setDefault ()' it affects all applications running on the same JVM
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        System.out.println(date);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        System.out.println(date);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println(date);

        System.out.println(date.getDate());
    }
}
