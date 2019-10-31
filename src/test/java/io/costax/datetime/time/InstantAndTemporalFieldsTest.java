package io.costax.datetime.time;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * we know that it is possible to refer to a specific point in the time line of the independent of the timezone via a Unix timestamp.
 * In the {@link java.time} API this concept is implemented by the class {@link java.time.Instant} .
 * <p>
 * - an instant has the amount nanoseconds since Unix Epoch.
 * - an instant doesn't have date and time fields, not timezone and offset.
 * - an instant has only the numeric value of the timestamp. How can this value correspond to a different date, time and offset in each timezone, these fields are not part of Instant
 */
class InstantAndTemporalFieldsTest {


    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static Clock DEFAULT_CLOCK = null;

    /**
     * Returns a {@link Clock} to simulate the current date.
     * <p>
     * The instant returned by {@link Clock} is May 4th, 2018 at 5:00 pm in Lisbon.
     */
    private static Clock clockMock() {
        if (DEFAULT_CLOCK == null) {
            ZonedDateTime z = ZonedDateTime.of(
                    2019,
                    Month.OCTOBER.getValue(),
                    19,
                    23, 20, 30, 352_133_000, ZoneId.of("Europe/Lisbon"));
            DEFAULT_CLOCK = Clock.fixed(z.toInstant(), z.getZone());
        }
        return DEFAULT_CLOCK;
    }

    /**
     * java.time classes have precision of nanoseconds so how to get the values with the precision maximum?
     * <p>
     * <p>
     * {@link java.time.Instant} stores the timestamp value in two fields:
     *
     * <pre>
     * - seconds: containing the number of seconds since Unix Epoch (he number of seconds from the epoch of 1970-01-01T00:00:00Z),
     * - nanos: containing the nanoseconds(The number of nanoseconds, later along the time-line, from the seconds field. This is always positive, and never exceeds 999,999,999).
     *  </pre>
     * <p>
     * <p>
     * Each of these fields has its own getter itself:
     * <pre>
     * {@link Instant#getEpochSecond}
     *
     * {@link Instant#getNano()}
     * </pre>
     * <p>
     * <p>
     * In this example, the timestampSeconds variable will contain the timestamp in seconds (in this case 1571523630),
     * while nanoseconds contains the value of nanoseconds (352_133_000).
     * When we call the {@link Instant#toEpochMilli()} method, these two values are combined to generate the timestamp in milliseconds.
     * That is, the nanoseconds are truncated and the last 6 houses decimals are lost (1571523630_352)
     */
    @Test
    void instant() {
        final Instant now = Instant.now(clockMock());

        System.out.println(now.getEpochSecond());
        System.out.println(now.getNano());
        System.out.println(now.toEpochMilli());

        assertEquals(1571523630L, now.getEpochSecond());
        assertEquals(352133000L, now.getNano());
        assertEquals(1571523630_352L, now.toEpochMilli());
    }

    @Test
    void instantOfTimestamp() {
        final Instant instant = Instant.ofEpochMilli(15_7152_3630_352L);

        assertEquals(15_7152_3630L, instant.getEpochSecond());
        assertEquals(352_000_000L, instant.getNano());
        assertEquals(15_715_23630_352L, instant.toEpochMilli());


        final Instant instantFromSecond = Instant.ofEpochSecond(157_152_363_0L);

        assertEquals(15_7152_3630L, instantFromSecond.getEpochSecond());
        assertEquals(000_000_000L, instantFromSecond.getNano());
        assertEquals(15_715_23630_000L, instantFromSecond.toEpochMilli());


        Instant instant2 = Instant.ofEpochSecond(15_7152_3639L, 123_456_789L);

        assertEquals(15_7152_3639L, instant2.getEpochSecond());
        assertEquals(123_456_789L, instant2.getNano());
        assertEquals(15_715_23639_123L, instant2.toEpochMilli());

    }

    /**
     * Local classes do not have enough information to Build an Instant.
     * In the following code we have a LocalDate, which only has the date.
     * <p>
     * To have a timestamp, we also need time and offset. So we have to
     * build a ZonedDateTime or an OffsetDateTime to so we get the instant:
     */
    @Test
    void instantFromLocalXXX() {
        final LocalDate localDate = LocalDate.now(clockMock());

        //final ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        final ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("Europe/Lisbon"));
        final Instant instant1 = zonedDateTime.toInstant();
        assertEquals(1571439600L, instant1.getEpochSecond());
        assertEquals(0, instant1.getNano());
        assertEquals(1571439600_000L, instant1.toEpochMilli());


        final OffsetDateTime offsetDateTime = localDate.atTime(0, 0).atOffset(ZoneOffset.of("+01:00"));
        final Instant instant2 = offsetDateTime.toInstant();
        assertEquals(1571439600L, instant2.getEpochSecond());
        assertEquals(0, instant2.getNano());
        assertEquals(1571439600_000L, instant2.toEpochMilli());
    }

    @Test
    void changeFieldsInInstant() {
        final Instant instant = Instant.now(clockMock());
        Assertions.assertEquals(1571523630352L, instant.toEpochMilli());

        final Instant instant1 = instant.with(ChronoField.MILLI_OF_SECOND, 987);
        Assertions.assertEquals(1571523630987L, instant1.toEpochMilli());
    }

    @Test
    void temporalFields() {
        final ZonedDateTime now = ZonedDateTime.now(clockMock());

        assertEquals(352, now.getLong(ChronoField.MILLI_OF_SECOND));
        assertTrue(isWeekend(now));
    }

    boolean isWeekend(TemporalAccessor temporal) {
        // get the day of the week
        DayOfWeek diaDaSemana = DayOfWeek.from(temporal);
        //	check if is one of the weekend days
        return WEEKEND.contains(diaDaSemana);
    }

    @Test
    void temporalQuery() {
        TemporalQuery<Boolean> workingDay = temporal -> {
            final int dayOfTheWeek = temporal.get(ChronoField.DAY_OF_WEEK);
            return DayOfWeek.SATURDAY.getValue() != dayOfTheWeek && DayOfWeek.SUNDAY.getValue() != dayOfTheWeek;
        };

        final Instant instant = Instant.now(clockMock());
        final ZonedDateTime zonedDateTime = ZonedDateTime.now(clockMock());

        Assertions.assertThrows(java.time.temporal.UnsupportedTemporalTypeException.class, () -> instant.query(workingDay));
        Assertions.assertFalse(zonedDateTime.query(workingDay));
    }
}
