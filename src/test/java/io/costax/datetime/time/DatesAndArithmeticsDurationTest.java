package io.costax.datetime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

class DatesAndArithmeticsDurationTest {

    @Test
    void plus1DayVsPlus24Hours() {
        // we know that for the time zone "Europe/Lisbon" a GAP happens at 31-MARCH-2019T01:00 to 31-MARCH-2019T02:00
        ZonedDateTime z = ZonedDateTime.of(
                LocalDate.of(2019, Month.MARCH, 30),
                LocalTime.of(2, 30, 45, 123_456_789)
                , ZoneId.of("Europe/Lisbon"));

        /*
         * when we have a date with timezone plus 1 day could be different of plus 24 hours
         */
        final ZonedDateTime zPlus1Day = z.plusDays(1);
        Assertions.assertEquals(LocalTime.of(2, 30, 45, 123_456_789), zPlus1Day.toLocalTime());
        final ZonedDateTime zPlus24Hours = z.plusHours(24);
        Assertions.assertEquals(LocalTime.of(3, 30, 45, 123_456_789), zPlus24Hours.toLocalTime());


        /*
         * For LocalDates we don't have timezone information, so, plus 24 hours produces the same result as plus 1 day.
         *
         * When using a ZonedDateTime, be aware of these cases.
         * If you are using any other class that does not have a timezone (including OffsetDateTime, which only has offset) this worry do not exists
         */
        final LocalDateTime d = LocalDateTime.of(
                LocalDate.of(2019, Month.MARCH, 30),
                LocalTime.of(2, 30, 45, 123_456_789));
        Assertions.assertEquals(LocalTime.of(2, 30, 45, 123_456_789), d.plusDays(1).toLocalTime());
        Assertions.assertEquals(LocalTime.of(2, 30, 45, 123_456_789), d.plusHours(24).toLocalTime());
    }

    /**
     * {@link java.time.temporal.TemporalField} represent a specific date field of date or hour (day, month, year, hour, minute, secound etc).
     * <pre>
     * -{@link java.time.temporal.ChronoField} is useful for getting fields other than have a specific getter
     * </pre>
     * <p>
     * <p/>
     * <p/>
     * <p>
     * {@link java.time.temporal.TemporalUnit} represents a unit of elapsed time (one specific field of a duration).
     * The API has some implementations of TemporalUnit, and the one with the units The most common of everyday life is
     * {@link java.time.temporal.ChronoUnit}.
     * <pre>
     * - {@link java.time.temporal.ChronoUnit} is useful for adding or subtract units for which there is no plus method or specific minus.
     * </pre>
     * <p>
     * <p/>
     * <p/>
     * <p>
     * ChronoUnit.MILLIS is the unit that corresponds to a duration in milliseconds. She is not the same as
     * ChronoField.MILLI_OF_SECOND (representing the field of milliseconds of a time).
     * Although the names are very similar, the conceptual difference between them is the same as between durations and dates.
     */
    @Test
    void differenceBetweenTemporalFieldAndTemporalUnit() {
        //	10:30:00.123456789
        LocalTime localTime = LocalTime.of(10, 30, 0, 123456789);

        //	plus 200 milliseconds -> 10:30:00.323456789
        LocalTime plus200Millis = localTime.plus(200, ChronoUnit.MILLIS);

        /*
         * When adding 200 milliseconds, the plus() method converts the value to 200_000_000 nanoseconds
         * and then makes the sum resulting in 10:30:00.323456789
         */
        Assertions.assertEquals(123_456_789L, localTime.getLong(ChronoField.NANO_OF_SECOND));
        Assertions.assertEquals(323_456_789L, plus200Millis.getLong(ChronoField.NANO_OF_SECOND));
    }

    /**
     * What happen when we try to add 1 month to a LocalTime?
     * <code>
     * LocalTime hour =	LocalTime.now().plus(1,	ChronoUnit.MONTHS);
     * </code>
     * <p>
     * LocalTime has no date fields, so, there no way add months. Therefore, this code throws an exception, saying
     * This unit is not supported: "java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Months"
     * <p>
     * We can check if a TemporalUnit is supported using isSupported() method (same as we used with
     * TemporalField)
     */
    @Test
    void cautionsWhenAddingUnsupportedUnits() {
        final UnsupportedTemporalTypeException unsupportedTemporalTypeException = Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> LocalTime.now().plus(1, ChronoUnit.MONTHS));
        Assertions.assertEquals("Unsupported unit: Months", unsupportedTemporalTypeException.getMessage());

        final boolean supported = LocalTime.now().isSupported(ChronoUnit.MONTHS);
        Assertions.assertFalse(supported);
    }

    /* ********************************************
     * SPECIFIC CLASSES FOR DURATIONS
     *
     * In the java.time API exists two classes than can be used to represent Durations.
     *
     * {@link java.time.Period}	e {@link java.time.Duration}
     */

    /**
     * The {@link Period} class represents a duration in terms of years months and days.
     * We can create it from the numeric values of each of these units, or parsing a String  in ISO 8601 format.
     * In the following code we have an example of each:
     */
    @Test
    void createPeriodInstance() {
        final Period p1Y2M20D = Period.parse("P1Y2M20D");
        final Period period = Period.of(1, 2, 20);

        Assertions.assertEquals(p1Y2M20D, period);
    }

    /**
     * The {@link Duration} class represents a duration in terms of  seconds and nanoseconds.
     * It is possible to create it from minutes, hours and days, and internally these values are converted to seconds.
     * Another way to create a Duration is parsing a String in "ISO format 8601".
     * In the following code we have an example of each:
     */
    @Test
    void createDurationInstance() {
        final Duration ofNanos1 = Duration.of(123_456_789L, ChronoUnit.NANOS);
        final Duration ofNanos2 = Duration.parse("PT0.123456789S");

        Assertions.assertEquals(ofNanos1, ofNanos2);

        final Duration ofHour1 = Duration.ofHours(1).plusMinutes(30L).plusSeconds(15L);
        final Duration ofHour2 = Duration.parse("PT1H30M15S");

        Assertions.assertEquals(ofHour1, ofHour2);
    }

    /**
     * There are two main ways to calculate the difference between two objects representing date and/or time.
     */
    @Test
    void calculateDifferencesBetweenDates() {

        // method 1
        LocalDate start = LocalDate.of(2018, 1, 1);
        LocalDate ends = LocalDate.of(2018, 1, 10);

        /*
         In this example we calculate how many days there are between 1 and 10 January 2018.
         The result is 9 because the calculation always includes the first date and excludes the last
         (start	inclusive,	end	exclusive)
         */
        long howManyDays = ChronoUnit.DAYS.between(start, ends); //	9
        Assertions.assertEquals(9, howManyDays);


    }
}
