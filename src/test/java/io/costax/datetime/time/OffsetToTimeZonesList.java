package io.costax.datetime.time;

import io.costax.datetime.Setup;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.costax.datetime.Setup.clock;

/**
 * Question: Given the offset {@code +02:00}, how do you know the timezone?
 * <p>
 * <p>
 * Answer:
 * <p>
 * It depends!
 * <p>
 * The answer depends on the date and time you use as reference.
 * <p>
 * For example, if you use "May 4, 2018, at 5:00 pm  in Sao Paulo, "will have a list of 57 timezones that use offset
 * {@code +02:00} on this day and time.
 * <p>
 * But if I change the reference date  to "October 31, 2018 at 5:00 pm in Sao Paulo" only 44 timezones.
 */
public class OffsetToTimeZonesList {


    @Test
    void all_ZoneId_with_the_same_offset_using_a_date() {

        // O horário de inverno começa neste dia Os relógios devem ser retrocedidos em uma hora das 00:00 para as 23:00 horas.
        //  Mais uma hora = portanto mais uma hora de sono!
        // 41 -	Na noite de Sábado 26 Outubro 2019 para Domingo 27 Outubro 2019


        // O horário de verão começa neste dia /Os relógios devem ser adiantados em uma hora das 00:00 para as 01:00 horas.
        // 195	Na noite de Sábado 28 Março 2020 para Domingo 29 Março 2020


        // inverno - [ 26-10-2019 : 28-03-2020 ]
        // Na noite de Sábado 28 Março 2020 para Domingo 29 Março 2020, das 00:00 para as 01:00 horas. Uma hora a menos = portanto uma hora de sono a menos!
        // verao -   [ 29-03-2020 : 24-10-2020 ]
        // Na noite de Sábado 24 Outubro 2020 para Domingo 25 Outubro 2020 / O horário de inverno começa neste dia Os relógios devem ser retrocedidos em uma hora das 00:00 para as 23:00 horas. Mais uma hora = portanto mais uma hora de sono!

        // 4 de Maio de 2018, às 17:00 em Lisboa
        ZonedDateTime maio = ZonedDateTime.now(clock());

        // Inverno
        final ZonedDateTime winter = ZonedDateTime.of(
                LocalDate.of(2019, 11, 1),
                LocalTime.of(13, 30, 0),
                ZoneId.of(Setup.EUROPE_LISBON));


        // Verão
        final ZonedDateTime summer = ZonedDateTime.of(
                LocalDate.of(2020, 5, 1),
                LocalTime.of(13, 30, 0),
                ZoneId.of(Setup.EUROPE_LISBON));

        System.out.println(winter.getOffset());
        System.out.println(summer.getOffset());


        // LocalDateTime -> ZonedDateTime

        final Set<ZoneId> zoneIdsWithTheSameOffSetInWinter = allZonesIdWithTheSameOffSetAt(winter);
        final Set<ZoneId> zoneIdsWithTheSameOffSetInSummer = allZonesIdWithTheSameOffSetAt(summer);

        System.out.println("---- " + winter.getZone() + " with the offset: " + winter.getOffset() + " -- total: " + zoneIdsWithTheSameOffSetInWinter.size());
        zoneIdsWithTheSameOffSetInWinter.forEach(System.out::println);

        System.out.println("---- " + summer.getZone() + " with the offset: " + summer.getOffset() + " -- total: " + zoneIdsWithTheSameOffSetInSummer.size());
        zoneIdsWithTheSameOffSetInSummer.forEach(System.out::println);
    }

    private Set<ZoneId> allZonesIdWithTheSameOffSetAt(final ZonedDateTime zonedDateTime) {
//        return ZoneId.getAvailableZoneIds().stream()
//                .map(ZoneId::of)
//                .filter(zoneId ->  zoneId.getRules().isDaylightSavings(zonedDateTime.toInstant()))
//                .collect(Collectors.toSet());

        return ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .map(zone -> zonedDateTime.toLocalDateTime().atZone(zone))
                .filter(z -> z.getOffset().equals(zonedDateTime.getOffset()))
                .map(ZonedDateTime::getZone)
                .collect(Collectors.toSet());
    }

    @Test
    void allTimeZones() {
        final String[] availableIDs = TimeZone.getAvailableIDs();
        Stream.of(availableIDs).forEach(System.out::println);
    }
}
