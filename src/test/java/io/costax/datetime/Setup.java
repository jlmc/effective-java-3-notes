package io.costax.datetime;

import java.time.Clock;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.TimeZone;

public class Setup {

    public static final String LANGUAGE = "pt";
    public static final String COUNTRY = "PT";
    public static final String EUROPE_LISBON = "Europe/Lisbon";

    // String language, String country
    private static Clock DEFAULT_CLOCK = null;

    /**
     * Setting the default settings so you don't have to change your JVM.
     * <p>
     * If you do not want to use this method, configure your JVM with the options below:
     *
     * <pre>
     * -Duser.country=PT -Duser.language=pt -Duser.timezone=Europe/Lisbon
     * </pre>
     */
    public static void setup() {
        Locale.setDefault(new Locale(LANGUAGE, COUNTRY));
        TimeZone.setDefault(TimeZone.getTimeZone(EUROPE_LISBON));
    }

    /**
     * Returns a {@link Clock} to simulate the current date.
     * <p>
     * The instant returned by {@link Clock} is May 4th, 2018 at 5:00 pm in Lisbon.
     */
    public static Clock clock() {
        if (DEFAULT_CLOCK == null) {
            ZonedDateTime z = ZonedDateTime.of(
                    2018,
                    Month.MAY.getValue(),
                    4,
                    17, 0, 0, 0, ZoneId.of(EUROPE_LISBON));
            DEFAULT_CLOCK = Clock.fixed(z.toInstant(), z.getZone());
        }
        return DEFAULT_CLOCK;
    }
}

