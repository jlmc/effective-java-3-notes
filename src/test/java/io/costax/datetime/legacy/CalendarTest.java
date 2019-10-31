package io.costax.datetime.legacy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarTest {

    @Test
    void calendarTest() {

        // the getInstance create a new instance of calendar with the current time
        final Calendar instance = Calendar.getInstance();

        // to set the calendar fields we can use the set method
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.MONTH, Calendar.JANUARY);
        instance.set(Calendar.YEAR, 2001);
        instance.set(Calendar.HOUR_OF_DAY, 22);
        instance.set(Calendar.MINUTE, 10);
        instance.set(Calendar.SECOND, 15);
        instance.set(Calendar.MILLISECOND, 0);
        // System.out.println(instance);

        // You can also get the same result by setting multiple fields at the same time
        instance.set(2019, Calendar.MAY, 24);
        // Or also setting the hours
        instance.set(1990, Calendar.NOVEMBER, 11, 9, 31, 0);
        instance.set(Calendar.MILLISECOND, 0);
    }

    /**
     * As already mentioned, Calendar has a timezone.
     * When you call the getInstance () method, Calendar created uses the standard JVM timezone, and the corresponding fields
     * (day, month, hour, minute, etc.) reflect the current date and time value  in this timezone.
     * <p>
     * If you change the timezone using the method setTimeZone (), field values are recalculated.
     * At the next example we have a calendar that uses timezone
     * default (Europe /Lisbon), where the current time is 1:00. To
     * change timezone to Europe/Berlin time now is 2:00.
     * <p>
     * Notes
     * <p>
     * - that the the getTimeInMillis() method keep return the same value.
     * <p>
     * - The timestamp value remains the same as Calendar continues to refer to the same instant -
     * only date and local time for the timezone used were updated.
     * <p>
     * Therefore, the Date returned by cal.getTime () will be  the same before and after changing the timezone.
     * Remember that Date has no timezone, only the timestamp value, and this  value does not change if we change the timezone
     */
    @Test
    void timezonesWithCalendar() {
        final Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Lisbon")));
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        myCalendar.set(Calendar.MONTH, 1);
        myCalendar.set(Calendar.YEAR, 2019);
        myCalendar.set(Calendar.HOUR_OF_DAY, 1);
        myCalendar.set(Calendar.MINUTE, 0);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);

        Assertions.assertEquals(1, myCalendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(1548982800000L, myCalendar.getTimeInMillis());

        myCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

        Assertions.assertEquals(2, myCalendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(1548982800000L, myCalendar.getTimeInMillis());
    }

    /**
     * By default the calendar instance are lenient, this means that if a excess value was used as parameter the calendar do the adjustment.
     * <p>
     * we can change this behavior, using the method:
     */
    @Test
    void calendarLenient() {

        final Calendar lenientCalendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Lisbon")));
        lenientCalendar.set(Calendar.DAY_OF_MONTH, 1);
        lenientCalendar.set(Calendar.MONTH, 1);
        lenientCalendar.set(Calendar.YEAR, 2019);
        lenientCalendar.set(Calendar.HOUR_OF_DAY, 25);
        lenientCalendar.set(Calendar.MINUTE, 0);
        lenientCalendar.set(Calendar.SECOND, 0);
        lenientCalendar.set(Calendar.MILLISECOND, 0);

        Assertions.assertEquals(2, lenientCalendar.get(Calendar.DAY_OF_MONTH));
        Assertions.assertEquals(1, lenientCalendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertTrue(lenientCalendar.isLenient());

        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.set(Calendar.DAY_OF_MONTH, 33);
    }
}
