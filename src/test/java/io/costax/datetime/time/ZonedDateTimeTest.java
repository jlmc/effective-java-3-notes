package io.costax.datetime.time;

import io.costax.datetime.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ZonedDateTime also has timezone information,this information is not discarded (as with classes local)
 */
class ZonedDateTimeTest {

    private Clock clock;

    @BeforeEach
    void setUp() {
        ZonedDateTime z = ZonedDateTime.of(
                2019,
                Month.MAY.getValue(),
                24,
                23, 20, 15, 123_000_000,
                ZoneId.of("Europe/Lisbon"));
        this.clock = Clock.fixed(z.toInstant(), z.getZone());
    }

    /**
     * The {@link ZonedDateTime#toString()} method return something like "2019-05-24T23:20:15.123+01:00[Europe/Lisbon]"
     *
     * <pre>
     * - Timezone contains the history of offsets of a region (which may vary over time) and it's important to know what
     * offset used at any given time.
     *
     * - The second point is that the toString() method also show timezone name in square brackets.
     *  This is very important because more than one timezones can use the same offset simultaneously, and if it were
     *  shown only +01:00, we wouldn't know which timezone being used
     *
     * - Remember that the ISO 8601 format only allows <b>offsets</b> and does not define a way to represent the <b>timezone</b> name.
     * The API {@link java.time}, on the other hand, extends this format by showing all information, avoiding any doubt as to the
     * {@link ZonedDateTime} content
     * </pre>
     */
    @Test
    void zoneDateTimeContainTheZoneIdAndTheOffset() {
        ZonedDateTime todayNow = ZonedDateTime.now(clock);
        Assertions.assertNotNull(todayNow.getZone());
        Assertions.assertNotNull(todayNow.getOffset());
    }

    /**
     * If you want to make it explicit in your code that timezone
     * JVM default should be used, just use {@link ZoneId#systemDefault()}
     */
    @Test
    void zone() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2019,
                Month.JANUARY.getValue(), 1, 1, 2, 3, 123_000_000,
                ZoneId.systemDefault());
        System.out.println(zonedDateTime);
    }

    /**
     * For the next examples I will use the {@link ZonedDateTime#of(LocalDate, LocalTime, ZoneId)} method,
     * which receives the numeric values date and time in addition to ZoneId to create ZonedDateTime
     * corresponding.
     * <p>
     * Basically this method will check which offset used by timezone on that day and time, and use these values
     * to create ZonedDateTime. But there are factors like summer (or any other offset changes), which can bring about
     * different results than expected.
     * <p>
     * Let's see how the class behaves when there's an STD gap, therefore is when daylight saving time begins and an hour is skipped.
     * <p>
     * <p>
     * We know that in mainland Portugal (Europe / Lisbon) the legal time in mainland Portugal the offset will be:
     * <pre>
     *  - Advanced 60 minutes to 1 hour legal time (1 hour UTC) on 31st March.
     *  and
     * - Delay 60 minutes at 2 hours legal time (1 hour UTC) on 27 October.
     * </pre>
     *
     * <p>
     * <p>
     * Therefore, at timezone Europe / Lisbon, March 31, 2019,
     * at 1 o'clock, the clocks were advanced one hour, directly to 2:00.
     * This means that an hour has been skipped and every minute between 01:00 and 01:59 there is no nestedia,
     * in this timezone.
     * <p>
     * That is, this date, time, and timezone combination (March 31, 2019, 01:01, Europe / Lisbon) is invalid.
     * <p>
     * Therefore it is set to the next valid time in case 02:01
     * </p>
     */
    @Test
    void gaps() {

        // HORA DE INVERNO E VERÃO PARA 2019

        /*
         * Portugal continental
         *
         * Em conformidade com a legislação, a hora legal em Portugal continental:
         *
         * Será:
         *
         * adiantada  60 minutos à 1 hora de tempo legal (1 hora UTC) do dia 31 de Março 2019
         *
         * e
         *
         * atrasada  60 minutos às 2 horas de tempo legal (1 hora UTC) do dia 27 de Outubro 2019.
         */

        /*
         * No timezone Europe/Lisbon, 31 de Março	de 2019,
         * à 1 hora, os relógios foram adiantados em uma hora, diretamente para 02:00.
         * Isso quer dizer que uma	hora foi pulada e todos os minutos entre 01:00 e 01:59 não existem nestedia,
         * neste timezone.
         *
         * Ou seja, esta combinação de data, hora e timezone (31 de Março de 2019, 01:01, Europe/Lisbon) é inválida.
         *
         * Por isso, ela é ajustada para o próximo	horário válido no caso, 02:01
         */

        final LocalDate date = LocalDate.of(2019, Month.MARCH, 31);
        final ZoneId zone = ZoneId.of("Europe/Lisbon");

        final ZonedDateTime beforeTheGap = ZonedDateTime.of(date, LocalTime.of(0, 59), zone);
        final ZonedDateTime fixedDateWithGap0 = ZonedDateTime.of(date, LocalTime.of(1, 0), zone);
        final ZonedDateTime fixedDateWithGap1 = ZonedDateTime.of(date, LocalTime.of(1, 1), zone);

        assertEquals(ZoneOffset.ofHours(1), fixedDateWithGap0.getOffset());
        assertEquals(ZoneOffset.ofHours(0), beforeTheGap.getOffset());

        assertEquals(LocalTime.of(0, 59), beforeTheGap.toLocalTime());
        assertEquals(LocalTime.of(2, 0), fixedDateWithGap0.toLocalTime());
        assertEquals(LocalTime.of(2, 1), fixedDateWithGap1.toLocalTime());
    }

    /**
     * Now let's see how ZonedDateTime behaves when
     * there is a daylight saving time overlap.
     * <p>
     * The time passes twice by one hour, ie The time 1H: 0M exists twice on the day [2019-OCTOBER-27]
     */
    @Test
    void overlaps() {
        /*
         * Portugal continental
         *
         * Em conformidade com a legislação, a hora legal em Portugal continental:
         *
         * Será:
         *
         * adiantada  60 minutos à 1 hora de tempo legal (1 hora UTC) do dia 31 de Março 2019
         *
         * e
         *
         * atrasada  60 minutos às 2 horas de tempo legal (1 hora UTC) do dia 27 de Outubro 2019.
         */

        final LocalDate date = LocalDate.of(2019, Month.OCTOBER, 27);
        final ZoneId zone = ZoneId.of("Europe/Lisbon");

        final ZonedDateTime beforeTheOverlaps1 = ZonedDateTime.of(date, LocalTime.of(0, 59), zone);
        assertEquals(ZoneOffset.ofHours(1), beforeTheOverlaps1.getOffset());

        final ZonedDateTime zonedDateTime1 = beforeTheOverlaps1.plusMinutes(1);
        assertEquals(ZoneOffset.ofHours(1), zonedDateTime1.getOffset());

        final ZonedDateTime zonedDateTime2 = zonedDateTime1.plusMinutes(1);
        assertEquals(ZoneOffset.ofHours(1), zonedDateTime2.getOffset());

        final ZonedDateTime oneHourAnd59MinBefore = beforeTheOverlaps1.plusHours(1);
        System.out.println(oneHourAnd59MinBefore);
        assertEquals(ZoneOffset.ofHours(1), oneHourAnd59MinBefore.getOffset());
        assertEquals(LocalTime.of(1, 59), oneHourAnd59MinBefore.toLocalTime());

        // The overlaped hour.
        // O horario passa duas vezes pela uma hora, ou seja, O hora 1H:0M existe duas vezes, no dia [2019-OCTOBER-27]
        final ZonedDateTime overlaped = oneHourAnd59MinBefore.plusMinutes(1);
        assertEquals(ZoneOffset.ofHours(0), overlaped.getOffset());
        assertEquals(LocalTime.of(1, 0), overlaped.toLocalTime());
    }

    /**
     * There is also the
     * <p>
     * {@link ZonedDateTime#withEarlierOffsetAtOverlap()}
     * <p>
     * method,  which returns the first occurrence. It is useful if you want to have
     * sure ZonedDateTime will refer to the first occurrence
     * local time in case of overlap.
     * <p>
     * In the case of the method {@link ZonedDateTime#of(LocalDate, LocalTime, ZoneId)},
     * we already know that by default it uses the first occurrence,
     * but if your code receives a ZonedDateTime that was created
     * somewhere else and you're not sure about the value you can use
     * this method to adjust it.
     * <p>
     * <p>
     * The {@link ZonedDateTime#withEarlierOffsetAtOverlap} and {@link ZonedDateTime#withLaterOffsetAtOverlap}
     * gives us more control over overlap situations when a location is ambiguous.
     * - The advantage of these methods is that you do not need to know if the overlap is one hour,
     * half an hour or whatever  another value.
     * - The methods are based on the timezone rules in question and calculate the correct offset for each case.
     * And when a date and time are not part of an overlap, both methods return the same ZonedDateTime, with the same
     * unchanged values. So it's okay to use them on cases where there is no overlap.
     */
    @Test
    void withEarlierOrLaterOffsetAtOverlap() {

        final LocalDate date = LocalDate.of(2019, Month.OCTOBER, 27);
        final ZoneId zone = ZoneId.of("Europe/Lisbon");

        final ZonedDateTime beforeTheOverlaps1 = ZonedDateTime.of(date, LocalTime.of(0, 59), zone);
        assertEquals(ZoneOffset.ofHours(1), beforeTheOverlaps1.getOffset());

        final ZonedDateTime oneHourAnd59MinBefore = beforeTheOverlaps1.plusHours(1);
        System.out.println(oneHourAnd59MinBefore);
        assertEquals(ZoneOffset.ofHours(1), oneHourAnd59MinBefore.getOffset());
        assertEquals(LocalTime.of(1, 59), oneHourAnd59MinBefore.toLocalTime());

        final ZonedDateTime overlaped = oneHourAnd59MinBefore.plusHours(1);
        assertEquals(ZoneOffset.ofHours(0), overlaped.getOffset());
        assertEquals(LocalTime.of(1, 59), overlaped.toLocalTime());

        // verifica se as instâncias têm exactemente a mesma data, time zoneId e Offset
        assertNotEquals(overlaped, oneHourAnd59MinBefore);

        // verifica se as instâncias correspondem ao mesmo instante
        assertFalse(overlaped.isEqual(oneHourAnd59MinBefore));

        // verify the offsets
        assertEquals(overlaped.toLocalDate(), oneHourAnd59MinBefore.toLocalDate());
        assertEquals(overlaped.toLocalTime(), oneHourAnd59MinBefore.toLocalTime());
        assertTrue(overlaped.isAfter(oneHourAnd59MinBefore));
        assertTrue(overlaped.isAfter(oneHourAnd59MinBefore));
        System.out.println(overlaped.toInstant().isAfter(oneHourAnd59MinBefore.toInstant()));
        System.out.println(overlaped.isAfter(oneHourAnd59MinBefore));

        // using the method withEarlierOffsetAtOverlap and withLaterOffsetAtOverlap
        assertEquals(oneHourAnd59MinBefore, overlaped.withEarlierOffsetAtOverlap());
        assertEquals(overlaped, oneHourAnd59MinBefore.withLaterOffsetAtOverlap());

        // is a new instance each operation we made
        assertNotSame(oneHourAnd59MinBefore, overlaped.withEarlierOffsetAtOverlap());
    }

    @Test
    void localDateToZonedDateTime() {
        final LocalDate localDate = LocalDate.of(2019, Month.OCTOBER, 19);

        final ZoneId europeLisbon = ZoneId.of("Europe/Lisbon");
        final ZonedDateTime actual0 = localDate.atStartOfDay().atZone(europeLisbon);
        final ZonedDateTime actual1 = localDate.atStartOfDay(europeLisbon);
        assertEquals(LocalTime.of(0, 0), actual0.toLocalTime());
        assertEquals(LocalTime.of(0, 0), actual1.toLocalTime());

        // Mostly the time will be at midnight
        // But if midnight is part of a timezone gap, the start of the day can be a different time.

        //	2017-10-15 - Daylight Saving Time Starts in Sao Paulo
        ZoneId americaSaoPaulo = ZoneId.of("America/Sao_Paulo");
        LocalDate date1 = LocalDate.of(2017, 10, 15);
        ZonedDateTime atStartOfDay = date1.atStartOfDay(americaSaoPaulo);
        System.out.println(atStartOfDay);

        assertEquals(LocalTime.of(1, 0), atStartOfDay.toLocalTime());
    }


    /**
     * We can work only with  the offset
     * <p>
     * {@link OffsetDateTime}
     * <p>
     * The difference is that {@link OffsetDateTime} doesn't have a timezone.
     * (offset only), and is therefore unaffected by gaps and overlaps.
     * Your offset is always the same, unlike with {@link ZonedDateTime}, which adjusts the offset to the history of your
     * Timezone
     */
    @Test
    void workingOnlyWithOffset() {
        final LocalDate localDate = LocalDate.of(2019, Month.OCTOBER, 19);

        final ZoneId europeLisbon = ZoneId.of("Europe/Lisbon");
        final ZonedDateTime actual0 = localDate.atStartOfDay(europeLisbon);
        //final ZoneOffset zonedOffset = actual0.getOffset();

        final OffsetDateTime offsetDateTime = actual0.toOffsetDateTime();

        assertNotNull(offsetDateTime);
    }

    /**
     * Convert an OffsetDateTime back to the {@link ZonedDateTime} (ie keeping the same offset) is not such a trivial task.
     * <p>
     * <p>
     * multiple timezones may use the same offset at a given time.
     * To know which ones we have to go through the list of available timezones (returned by
     * {@link ZoneId#getAvailableZoneIds()}) and for each check the offset used right now.
     * <p>
     * <p>
     * To achieve this we can use the {@link OffsetDateTime#atZoneSameInstant(ZoneId)} method to convert {@link OffsetDateTime} to a timezone.
     * This method returns a {@link ZonedDateTime} that matches at the same time OffsetDateTime by setting the date,
     * time and offset automatically.
     * With this we can check if the offset used for another timezone is the same as our {@link OffsetDateTime}
     * <p>
     * Note:
     * This example only applies in cases where we want to convert a timezone that uses the exact same offset.
     * To convert for any timezone, just use the {@link OffsetDateTime#atZoneSameInstant(ZoneId)} method,
     * remembering that it can adjust the date, time and offset if necessary.
     */
    @Test
    void convertOffsetDateTimeToZonedDateTime() {
        final OffsetDateTime offsetDateTime = OffsetDateTime.now(Setup.clock());

       /* ZoneId.getAvailableZoneIds()
                .stream()
                .forEach(zoneName -> {

                    final ZoneId zone = ZoneId.of(zoneName);
                    final ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(zone);

                    if (offsetDateTime.getOffset().equals(zonedDateTime.getOffset())) {
                        System.out.println(zoneName);
                    }
                });*/

        final List<ZonedDateTime> allZonedDateTimeWithTheSameOffset =
                ZoneId.getAvailableZoneIds()
                        .stream()
                        .map(ZoneId::of)
                        .map(offsetDateTime::atZoneSameInstant)
                        .filter(zonedDateTime -> offsetDateTime.getOffset().equals(zonedDateTime.getOffset()))
                        .peek(System.out::println)
                        .collect(Collectors.toUnmodifiableList());

        assertEquals(33, allZonedDateTimeWithTheSameOffset.size());
    }

}
