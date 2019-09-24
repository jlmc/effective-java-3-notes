package io.costax.datetime.time;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;

/**
 * OBTAINING INFORMATION FROM TIMEZONE
 * <p>
 * From a {@link ZoneId} you can get various information about timezone's offsets history.
 * <p>
 * This information is encapsulated by the {@link java.time.zone.ZoneRules} class, which can get by {@link ZoneId#getRules()} method.
 * <p>
 * Having {@link ZoneRules}, we can check the history of all timezone transitions (every time there has been any offset change), using
 * {@link ZoneRules#getTransitions()}
 * <p>
 * <p>
 * The{@link ZoneRules#getTransitions()}  method returns a list of {@link java.time.zone.ZoneOffsetTransition},
 * a class that represents a transition (an offset change).
 */
class TimeZoneInformations {

    /**
     * The following code prints several lines of trans, one for each time the {@link java.time.zone.ZoneOffsetTransition}
     * changed in timezone "Europe/Lisbon"
     * <p>
     * Lets see two example lines:
     *
     * <pre>
     * - Transition[Overlap at 1996-10-27T02:00+01:00 to Z]
     * - Transition[Gap at 1997-03-30T01:00Z to +01:00]
     * </pre>
     * <p>
     * <p>
     * The first line indicates an overlap (when a time exists twice, once at each offset),
     * stating the date and time when  occurred in addition to the offsets used before and after the transition.
     * In this In this case it indicates the end of daylight saving time in October 1996:
     * two o'clock on the 30th the offset was -01:00 and changed to Z (00:00)
     *
     * <p>
     * <p>
     * The second line we have a gap that corresponds DST, and it also reports the date and time where it happens in addition
     * to the offsets before and after the transition.
     */
    @Test
    void timeZoneInformation() {
        final ZoneRules rules = ZoneId.of("Europe/Lisbon").getRules();

        rules.getTransitions().forEach(System.out::println);
    }

    @Test
    void howToKnoWhenWillBeTheNextOffsetChange() {

        Instant now = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 26),
                LocalTime.of(23, 0),
                ZoneId.of("Europe/Lisbon"))
                .toInstant();

        ZoneRules rules = ZoneId.of("Europe/Lisbon").getRules();
        ZoneOffsetTransition nextTransition = rules.nextTransition(now);

        System.out.println(nextTransition);

        ZoneOffsetTransition previousTransition = rules.previousTransition(Instant.now());


        //System.out.println(previousTransition);
    }
}
