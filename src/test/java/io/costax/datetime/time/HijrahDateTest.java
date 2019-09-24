package io.costax.datetime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

public class HijrahDateTest {

    /**
     * Ramadan 2016 began in the evening of
     * Monday, June 6
     * and ended in the evening of
     * Tuesday, July 5
     */
    @Test
    void ramadanOfYear2016() {
        final LocalDate reference = LocalDate.of(2016, Month.JANUARY, 1);

        //first day of Ramadan, 9th month
        ///HijrahDate.now().with(ChronoField.YEAR, ramdanDate.get(ChronoField.YEAR) - 1)

        final HijrahDate ramadan = HijrahDate.from(reference).with(ChronoField.DAY_OF_MONTH, 1).with(ChronoField.MONTH_OF_YEAR, 9);
        //HijrahDate ramadan = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1).with(ChronoField.MONTH_OF_YEAR, 9);
        System.out.println("HijrahDate : " + ramadan);

        //HijrahDate -> LocalDate
        System.out.println("\n--- Ramandan 2016 ---");
        final LocalDate ramandanStarts = LocalDate.from(ramadan);
        System.out.println("Start : " + ramandanStarts);

        //until the end of the month
        final LocalDate ramandanEnds = LocalDate.from(ramadan.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("End : " + ramandanEnds);

        Assertions.assertEquals(LocalDate.of(2016, Month.JUNE, 6), ramandanStarts);
        Assertions.assertEquals(LocalDate.of(2016, Month.JULY, 5), ramandanEnds);
    }
}
