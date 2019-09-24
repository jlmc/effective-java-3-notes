package io.costax.datetime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DurationTest {

    // 2019-03-30T22:30Z[Europe/Lisbon]
    final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.MARCH)
            .atDay(30)
            .atTime(22, 30, 0)
            .atZone(ZoneId.of("Europe/Lisbon"));

    @Test
    void diferenceBetween() {
        final ZonedDateTime zonedDateTime1 = YearMonth.of(2019, Month.JANUARY).atDay(1)
                .atTime(1, 0, 0)
                .atZone(ZoneId.of("Europe/Lisbon"));


        final ZonedDateTime zonedDateTime2 = YearMonth.of(2019, Month.FEBRUARY).atDay(1)
                .atTime(2, 0, 0)
                .atZone(ZoneId.of("Europe/Lisbon"));

        final Duration between = Duration.between(zonedDateTime1, zonedDateTime2);

        Assertions.assertEquals(745, between.toHours());
    }

    @Test
    void calculateAgeWithJavaTime() {
        System.out.println(new Date());
        System.out.println(Calendar.getInstance().getTime());

        LocalDate birthDay = LocalDate.of(1955, Month.MAY, 19);
        //LocalDate birthDay = LocalDate.of(1955, Month.MAY, 1);
        LocalDate today = LocalDate.of(2005, Month.JUNE, 30);

        System.out.println(ChronoUnit.MONTHS.between(birthDay, today));

        final Period between = Period.between(birthDay, today);

        System.out.println(between.getYears() * 12 + between.getMonths());

        // 50 + 1 mes
        // 601

        Assertions.assertEquals(601, ChronoUnit.MONTHS.between(birthDay, today));
    }

    @Test
    void calculateAgeWithCalendar() {
        Calendar birthDay = new GregorianCalendar(1955, Calendar.MAY, 19);
        //Calendar birthDay = new GregorianCalendar(1955, Calendar.MAY, 1);
        birthDay.set(Calendar.HOUR_OF_DAY, 0);
        birthDay.set(Calendar.MINUTE, 0);
        birthDay.set(Calendar.SECOND, 0);
        birthDay.set(Calendar.MILLISECOND, 0);


        Calendar today = new GregorianCalendar(2005, Calendar.JUNE, 30);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        birthDay.set(Calendar.MILLISECOND, 0);

        int yearsInBetween = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int monthsDiff = today.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);

        long ageInMonths = yearsInBetween * 12 + monthsDiff;
        long age = yearsInBetween;

        System.out.println("Number of months since James gosling born : " + ageInMonths);

        System.out.println("Sir James Gosling's age : " + age);
    }
}
