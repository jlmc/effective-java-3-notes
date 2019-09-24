package io.costax.datetime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * On the night of Saturday 30 March 2019 to Sunday 31 March 2019
 * <p>
 * Daylight Saving (Summer) Time begins on this day.
 * Clocks must be advanced one hour from 00:00 to 01:00 hours.
 * One hour less = therefore one hour less sleep!
 */
class AddDayOrAdd24HoursToDateTest {

    @Test
    void addDaysOrAddHoursToZonedDateSpecialCasesTest() {

        // 	On the night of Saturday 30 March 2019 to Sunday 31 March 2019

        // 2019-03-30T22:30Z[Europe/Lisbon]
        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.MARCH)
                .atDay(30).atTime(22, 30, 0)
                .atZone(ZoneId.of("Europe/Lisbon"));

        //2019-03-31T23:30+01:00[Europe/Lisbon]
        final ZonedDateTime zonedDateTimeAfterAdd24h = zonedDateTime.plusHours(24L);
        //2019-03-31T22:30+01:00[Europe/Lisbon]
        final ZonedDateTime zonedDateTimeAfterAdd1Day = zonedDateTime.plusDays(1L);

        /*
         * Because offset changes at midnight, adding 24 hours doesn't produce the same result as adding a day
         */
        assertFalse(zonedDateTimeAfterAdd24h.isEqual(zonedDateTimeAfterAdd1Day));
        assertFalse(zonedDateTimeAfterAdd24h.toInstant().toEpochMilli() == zonedDateTimeAfterAdd1Day.toInstant().toEpochMilli());


        /*
         * In normal cases, when there is no offset change, then adding 24 Hours produces the same result as adding 1 day.
         */

        // 2019-03-30T22:30Z[Europe/Lisbon]
        final ZonedDateTime normalCase = YearMonth.of(2019, Month.MARCH)
                .atDay(1).atTime(22, 30, 00)
                .atZone(ZoneId.of("Europe/Lisbon"));
        // 2019-03-02T22:30Z[Europe/Lisbon]
        final ZonedDateTime normalCasePlus1Day = normalCase.plusDays(1L);
        // 2019-03-02T22:30Z[Europe/Lisbon]
        final ZonedDateTime normalCasePlus24Hours = normalCase.plusHours(24L);

        Assertions.assertTrue(normalCasePlus1Day.isEqual(normalCasePlus24Hours));
        Assertions.assertEquals(normalCasePlus1Day.toInstant().toEpochMilli(), normalCasePlus24Hours.toInstant().toEpochMilli());

        // Note that this example is not valid for java.time.LocalDateTime, because this type pf java.time do not contain the offset ou ZoneId information
        // So add days or hours produces always the same result for java.time.LocalDateTime.
    }
}
