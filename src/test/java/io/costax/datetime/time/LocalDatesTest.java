package io.costax.datetime.time;

import io.costax.datetime.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 13.1 DATE CLASS AND LOCAL TIME
 * <p>
 * The classes {@link java.time.LocalDate}, {@link java.time.LocalTime} and {@link java.time.LocalDateTime}
 * do not contain any information about the timezone, so they are not affected by gaps and overlaps:
 * <p>
 * {@link java.time.LocalDate} - represent one date (day-month-year), without No information about the date or timezone.
 * <p>
 * {@link java.time.LocalTime} - only represents one time (hours, minutes, seconds and fractions of a second) without
 * No information about the date or timezone.
 * <p>
 * {@link java.time.LocalDateTime} - represents a date and time, no information about timezone.
 * In summary, it's a combining a LocalDate with a LocalTime.
 * <p>
 * <p>
 * All the LocalDate, LocalTime and LocalDateTime toString method return a string in ISO 8601 format.
 */
public class LocalDatesTest {

    private static final int ONE_MILLION = 1_000_000;

    @Test
    void doNotAllowedInvalidValues() {
        final DateTimeException dateTimeException = Assertions.assertThrows(DateTimeException.class, () -> LocalDate.of(2019, Month.FEBRUARY, 31));
        Assertions.assertEquals("Invalid date 'FEBRUARY 31'", dateTimeException.getMessage());
    }

    /**
     * In the java.time API precision has been increased to 9 digits (nanoseconds).
     * It is possible to create a LocalTime with 9 decimal places in fraction of seconds:
     * <p>
     * <p>
     * What if I want a value with lower precision?
     * For example,  I want the time to be 17:30:25.123
     * (the fraction value equal to 123 milliseconds).
     * Since LocalTime always saves the value in nanoseconds,
     * we have to multiply the milliseconds by 1 million to get the equivalent in nanoseconds.
     *
     * <p>
     * now () always returns the nanoseconds?
     * No. The implementation of Java 8 is based on {@link System#currentTimeMillis()}, ie has precision of milliseconds,
     * then {@link LocalDate#now()} returns values with no maximum 3 decimal places, such as 17:30:25.123.
     * Internally, the fractional second value will be 123000000 since the class always stores the value in nanoseconds. But the accuracy used
     * will be milliseconds, ie the last 6 digits will always be zero.
     */
    @Test
    void localDateTest() {
        LocalTime hour1 = LocalTime.of(17, 30, 25, 123456789);
        Assertions.assertNotNull(hour1);
        Assertions.assertEquals(hour1.getNano(), 123456789);

        final int mils = 123;
        final int nanos = mils * ONE_MILLION;

        //final long convert = TimeUnit.NANOSECONDS.convert(123, TimeUnit.MILLISECONDS);
        //final long convert = TimeUnit.MILLISECONDS.toNanos(123);
        //System.out.println(convert);

        // 17:30:25.123 (123 milliseconds = 123000000 nanoseconds)
        LocalTime hour2 = LocalTime.of(17, 30, 25, nanos);
        Assertions.assertEquals(123000000, hour2.getNano());
    }

    @Test
    void lastDayOfTheMonth() {
        LocalDate data = LocalDate.of(2018, Month.MAY, 4);

        //final LocalDate localDate = data.withDayOfMonth(data.getMonth().maxLength());
        final LocalDate lastDayOfMonth = data.with(TemporalAdjusters.lastDayOfMonth());

        Assertions.assertEquals(31, lastDayOfMonth.getDayOfMonth());
        Assertions.assertEquals(Month.MAY, lastDayOfMonth.getMonth());
        Assertions.assertEquals(2018, lastDayOfMonth.getYear());
        Assertions.assertEquals(DayOfWeek.THURSDAY, lastDayOfMonth.getDayOfWeek());
    }

    @Test
    void nextFriday() {
        LocalDate data = LocalDate.of(2018, Month.MAY, 4);

        final LocalDate nextFriday = data.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        Assertions.assertEquals(LocalDate.of(2018, 5, 11), nextFriday);

        final LocalDate lastSundayOfMonth = data.with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        Assertions.assertEquals(LocalDate.of(2018, 5, 27), lastSundayOfMonth);

        final LocalDate of1 = LocalDate.of(2018, 5, 11);
        final LocalDate nextFridayOrSame = of1.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        Assertions.assertEquals(LocalDate.of(2018, 5, 11), nextFridayOrSame);
        Assertions.assertSame(of1, nextFridayOrSame);

        LocalDate thirdThursdayOfTheMonth = LocalDate.of(2018, 5, 25).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.THURSDAY));
        Assertions.assertEquals(LocalDate.of(2018, 5, 17), thirdThursdayOfTheMonth);
    }

    /**
     * The {@link LocalDate}  and  {@link LocalDateTime} classes also have {@link LocalDate#isEqual(ChronoLocalDate)} method,
     * which is used to compare dates in other calendars.
     * <p>
     * There are several calendars in use around the world, and in each of them the values of the day-month-year are although all dates occur on the same
     * moment (while we are in 2018, the Islamic calendar is in 1439, for example).
     * <p>
     * Unless you need working with other calendars, using equals () is already the enough.
     */
    @Test
    void isEqualsMethodTest() {
        final HijrahDate hijrahDate = HijrahChronology.INSTANCE.dateNow(Setup.clock());
        final LocalDate localDate = LocalDate.now(Setup.clock());

        Assertions.assertFalse(localDate.equals(hijrahDate));
        Assertions.assertTrue(localDate.isEqual(hijrahDate));

        System.out.println(hijrahDate);
        System.out.println(localDate);
    }
}
