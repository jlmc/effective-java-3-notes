package io.costax.datetime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TimeZoneTest {

    /**
     * One problem with the TimeZone class is that if you pass a Invalid name for getTimeZone (), it returns an instance corresponding to UTC. In fact a timezone is created called "GMT" whose offset is zero and has no daylight saving time nor any other offset change - that is,
     * In practice, it is An instance of TimeZone that corresponds to UTC is created.
     * <p>
     * This is a problem because a simple typo can go unnoticed.
     * For example, in the following code, timezone Europe/Lisbonera was misspelled ("Lisbonera" instead of "Lisbon"):
     */
    @Test
    void getTimeZoneDoNotValidateTheName() {

        final TimeZone correct = TimeZone.getTimeZone("Europe/Lisbon");
        System.out.println(correct.getID());
        assertEquals("Europe/Lisbon", correct.getID());

        final TimeZone misspelled = TimeZone.getTimeZone("Europe/Lisbonera");
        assertEquals("GMT", misspelled.getID());
    }

    /**
     * One way to avoid the previous problem is to verify that the name you
     * you are using is one of the valid timezones available in the JVM.
     * The list of all timezones that the JVM recognizes is returned.
     * by the TimeZone.getAvailableIDs () method. Another way is
     * check if the getID () method returns the same name that was
     * passed to getTimeZone () method
     */
    @Test
    void checkTimeZoneId() {
        final String misspelledTimeZoneId = "Europe/Lisbonera";
        final TimeZone timeZone = TimeZone.getTimeZone(misspelledTimeZoneId);

        Assertions.assertNotEquals(timeZone.getID(), misspelledTimeZoneId);

        final boolean validTimeZoneId = Arrays.asList(TimeZone.getAvailableIDs()).contains(misspelledTimeZoneId);
        assertFalse(validTimeZoneId);
    }

    /**
     * The getTimeZone () method does not accept only the IANA identifiers.
     * <p>
     * It also accepts offsets, and the result is a TimeZone instance that has only a single offset.
     * <p>
     * There is only one caveat: offsets must have the prefix "GMT" before the its value, as shown in the next example.
     * // create a "timezone" that corresponds to offset +03:00
     * TimeZone offset = TimeZone.getTimeZone ("GMT+03:00");
     * <p>
     * We know that an offset is simply the difference from UTC, and a TimeZone is a list with the history of all offsets a region has had,
     * have and will have.
     * Nevertheless, the TimeZone class implements both Concepts.
     * <p>
     * - If the getTimeZone () method gets an identifier  returns an instance that has the history of offsets corresponding
     * <p>
     * - if this method receives an offset, the returned instance has just the offset value that was passed.
     */
    @Test
    void getTimeZoneUsingOffset() {
        TimeZone offset = TimeZone.getTimeZone("GMT+03:00");
        System.out.println(offset);
    }

    @Test
    void getAllTimeZones() {
        Stream.of(TimeZone.getAvailableIDs())
                .forEach(System.out::println);
    }

    /**
     * The method {@link TimeZone#getID()} returns the identifier of the IANA.
     * <p>
     * The method {@link TimeZone#getDisplayName()} give us a "friendly name", this method use the JVM to know the lacale to display the "friendly name"
     * If we need to display in an specific locale, we should use the method {@link TimeZone#getDisplayName(Locale)}
     */
    @Test
    void getBasicInformation() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Lisbon");
        System.out.println(zone.getID());
        System.out.println(zone.getDisplayName());

        assertEquals("Europe/Lisbon", zone.getID());
        assertEquals("Hora padrão da Europa Ocidental", zone.getDisplayName(new Locale("pt", "PT")));
    }

    /**
     * To find out what offset is used by a timezone on a date and
     * specific time, just use the {@link TimeZone#getOffset(long)} method, passing
     * as a parameter the numeric value of the timestamp.
     * <p>
     * Remember that a timezone has all the history of offsets of a given region. So the The getOffset() method must be instantiated as reference.
     * If you want to use the current timestamp, just pass the {@link System#currentTimeMillis()}.
     * If you have a Calendar, then you pass the value of {@link Calendar#getTimeInMillis()}.
     * <p>
     * The return of getOffset() is the offset value in milliseconds.
     * If you want to convert it to hours and minutes, just use {@link java.util.concurrent.TimeUnit}
     */
    @Test
    void getTheOffsets() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Lisbon");

        final long millisecondsSummer = YearMonth.of(2020, 10)
                .atDay(14)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Lisbon"))
                .toInstant()
                .toEpochMilli();

        final long millisecondsWinter = YearMonth.of(2020, 1)
                .atDay(14)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Lisbon"))
                .toInstant()
                .toEpochMilli();

        assertEquals(1L, TimeUnit.MILLISECONDS.toHours(zone.getOffset(millisecondsSummer)));
        assertEquals(0L, TimeUnit.MILLISECONDS.toHours(zone.getOffset(millisecondsWinter)));
    }

    /**
     * The TimeZone class has some methods for getting information about daylight saving time.
     * The simplest is {@link TimeZone#inDaylightTime(Date)}, which returns true if at the moment
     * represented by Date, the timezone is in daylight saving time.
     * this method is very useful because it allows you to consult any date on the past, present or future.
     */
    @Test
    void knowIfADateIsSummer() {
        final TimeZone zone = TimeZone.getTimeZone("Europe/Lisbon");

        final long millisecondsSummer = YearMonth.of(2020, 10)
                .atDay(14)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Lisbon"))
                .toInstant()
                .toEpochMilli();

        final long millisecondsWinter = YearMonth.of(2020, 1)
                .atDay(14)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Lisbon"))
                .toInstant()
                .toEpochMilli();

        assertTrue(zone.inDaylightTime(Date.from(Instant.ofEpochMilli(millisecondsSummer))));
        assertFalse(zone.inDaylightTime(Date.from(Instant.ofEpochMilli(millisecondsWinter))));
    }

    /**
     * There is also the getDSTSavings() method, which returns the
     * amount of milliseconds to add to local time,
     * when it's daylight saving time.
     */
    @Test
    void numberOfMillisToAddInSummerTime() {
        final TimeZone zone = TimeZone.getTimeZone("Europe/Lisbon");
        Assertions.assertEquals(3600000L, zone.getDSTSavings());
    }

    /**
     * As we have already seen, given an offset, it is not possible to have a single
     * Timezone as an answer. There is more than one timezone that uses the
     * same offset, and besides that there are changes all the time, either by
     * cause of daylight saving time or simply because a country
     * decided to change your local time.
     * Therefore, the list of timezones that uses certain offset may vary depending on the date on which we consulted.
     * <p>
     * In the following code, we will look for the timezones that use the
     * offset +02:00, using the current date as a reference.
     * <p>
     * 1. First, we have to convert the two hour offset to the equivalent value
     * in milliseconds, in order to compare it with the return of getOffset().
     * <p>
     * 2. And for the reference date, we use  System.currentTimeMillis (), as getOffset () receives
     * as a parameter the timestamp. As we only want the value
     * numeric.
     */
    @Test
    void givenAnOffsetHowToKnowTheTimezone() {
        //	offset +02:00 in millis
        long offset = TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS);

        final long references = YearMonth.of(2020, 10)
                .atDay(14)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Lisbon"))
                .toInstant()
                .toEpochMilli();

        final List<TimeZone> collect = Arrays.stream(TimeZone.getAvailableIDs())
                .map(TimeZone::getTimeZone)
                .filter(timeZone -> timeZone.getOffset(references) == offset)
                .collect(Collectors.toList());

        Assertions.assertEquals(60, collect.size());
        Assertions.assertTrue(collect.contains(TimeZone.getTimeZone("Europe/Amsterdam")));
    }

    /**
     * Which timezones use certain abbreviations?
     * <p>
     * Timezones abbreviations (such as EST, IST, among others) are ambiguous because more than one timezone can use them,
     * often in different countries.
     * So, given an abbreviation, there is no way to get a single Timezone And how many countries use a different abbreviation during daylight saving time, I also need to set a date of
     * reference. The best we can get is a list of timezones that use the abbreviation on a certain date.
     * <p>
     * To know the abbreviation used by each timezone on the date of reference,
     * We can use a SimpleDateFormat with the pattern 'z' (lower case), which stands for timezone abbreviation.
     * <p>
     * <p>
     * Some timezones that use the abbreviation "EST" (such as America/New_York and America/Toronto) will not be on final result,
     * as at the reference date (May 4, 2018, at 21:00 in Lisbon) they are in daylight saving time, so the The abbreviation being used
     * by them at this time is "EDT".
     * <p>
     * Of course that if you run this code on another day when these timezones are not in daylight saving time, they will be in the result Final.
     */
    @Test
    void givenATimeZoneAbbreviationsWhatAreTheTimeZonesWhatUseTheSameAbbreviation() {
        final String abbreviation = "EST";

        /*
        final OffsetDateTime reference = OffsetDateTime.of(YearMonth.of(2018, Month.MAY)
                .atDay(4)
                .atTime(17, 0), ZoneOffset.ofHours(-3));
        final ZonedDateTime zonedDateTime = reference.atZoneSameInstant();
         */

        final ZonedDateTime reference = YearMonth.of(2018, Month.MAY).atDay(4).atTime(21, 0).atZone(ZoneId.of("Europe/Lisbon"));
        final Date dateReference = Date.from(reference.toInstant());


        @SuppressWarnings("SimplifyStreamApiCallChains") final Set<String> timeZoneIds = Stream.of(TimeZone.getAvailableIDs())
                .filter(timeZoneId ->
                        Stream.of(Locale.getAvailableLocales())
                                .map(locale -> new SimpleDateFormat("z", locale))
                                .map(sdf -> {
                                    sdf.setTimeZone(TimeZone.getTimeZone(timeZoneId));
                                    return sdf;
                                })
                                .map(sdf -> sdf.format(dateReference))
                                .anyMatch(abbr -> abbr.equals(abbreviation)))
                .collect(Collectors.toUnmodifiableSet());

        /*
        Set<String> timeZoneIds = new HashSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            for (String timezoneId : TimeZone.getAvailableIDs()) {
                timezoneAbbreviationFormatter.setTimeZone(TimeZone.getTimeZone(timezoneId));
                if	(abbreviation.equals(timezoneAbbreviationFormatter.format(dateReference))) {
                    timeZoneIds.add(timezoneId);
                }
            }
        }
        */


        Assertions.assertEquals(9, timeZoneIds.size());
        Assertions.assertTrue(timeZoneIds.containsAll(
                List.of("America/Coral_Harbour",
                        "SystemV/EST5",
                        "America/Jamaica",
                        "EST",
                        "America/Cancun",
                        "America/Cayman",
                        "America/Atikokan",
                        "America/Panama",
                        "Jamaica")
        ));
    }

    /**
     * As an example, let's implement a use case as well
     * common, which is finding the next day of the week from a
     * date. In this case, let's start December 27, 2011, and the
     * from this date find next friday
     * <p>
     * We are going to use first the TimeZone "Europe/Lisbon"
     */
    @Test
    @DisplayName("12.3 THERE IS ALWAYS A TIMEZONE IN ACTION")
    void thereIsAlwaysATimeZoneInAction() {
        final String timeZoneId = "Europe/Lisbon";
        final ZonedDateTime reference = YearMonth.of(2011, Month.DECEMBER).atDay(27).atTime(23, 13, 0).atZone(ZoneId.of(timeZoneId));

        final Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setLenient(false);
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        //calendar.set(2011, Calendar.DECEMBER, 27);
        calendar.setTimeInMillis(reference.toInstant().toEpochMilli());

        // find the next friday
        do {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY);

        final Instant instant = Instant.ofEpochMilli(calendar.getTimeInMillis());
        final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(timeZoneId));

        // In this example the hour is 23:18:55 because the Calendar.getInstance method create a Calendar Instance with the current hour
        // and method calendar.add(Calendar.DAY_OF_MONTH, 1) keeps the hour, it just increment the day

        //"2011-12-30T23:18:55.747Z[Europe/Lisbon]"
        final ZonedDateTime expected = ZonedDateTime.of(2011, 12, 30, 23, 13, 0, 0, ZoneId.of(timeZoneId));

        Assertions.assertEquals(expected, zonedDateTime);
    }

    /**
     * The previous test, the result can be another, if we use a different timezone, for example: let consider the timezone "Pacific/Apia" in the same date
     * <p>
     * Now the result will be 2012-01-06T12:27:56.181+14:00[Pacific/Apia], but what happened? Why was there such a leap in time?
     * <p>
     * If you search for a reason, you will find out that the "December 30, 2011" was skipped due to the move to the across the International Date Line.
     * The day 30th was skipped and doesn't exist in that timezone, so it makes sense that it's not considered next Friday.
     * <p>
     * It is like {@link Calendar#add(int, int)} adjusts dates according to timezone rules, the 30th will be skipped,
     * and the result will be the following Friday (5 January 2012).
     */
    @Test
    void thereIsAlwaysATimeZoneInActionInPacific_Apia() {
        final String timeZoneId = "Pacific/Apia";
        final ZonedDateTime reference = YearMonth.of(2011, Month.DECEMBER).atDay(27)
                .atTime(23, 13, 0).atZone(ZoneId.of(timeZoneId));

        //final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        //calendar.set(2011, Calendar.DECEMBER, 27);

        final Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setLenient(false);
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        //calendar.set(2011, Calendar.DECEMBER, 27);
        calendar.setTimeInMillis(reference.toInstant().toEpochMilli());


        // find the next friday
        do {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY);

        final Instant instant = Instant.ofEpochMilli(calendar.getTimeInMillis());
        final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(timeZoneId));

        // In this example the hour is 23:18:55 because the Calendar.getInstance method create a Calendar Instance with the current hour
        // and method calendar.add(Calendar.DAY_OF_MONTH, 1) keeps the hour, it just increment the day
        //Assertions.assertNotEquals("2012-01-06T23:13+14:00[Pacific/Apia]", zonedDateTime.toString());
        Assertions.assertEquals("2012-01-06T23:13+14:00[Pacific/Apia]", zonedDateTime.toString());
    }

    /**
     * The previous test, the result can be another, even for situations of change to the Daylight Saving Time begins, for example: let consider the timezone "Europe/Lisbon"
     * in the date: "2019-MARCH-26'T'01:30"
     * <p>
     * We know that the Daylight Saving Time begins at the day "2019-MARCH-31'T'01:00"
     * <p>
     * The day is correct ("2019-03-31"), the time is not exactly as expected :)
     * <p>
     * We originally had an hour of 1:30 but the result was 0:30 (minus one hour).
     * This is because on March 30, 2019 daylight saving time begins,
     * (Clocks must be advanced one hour from 00:00 to 01:00). The calendar made the adjustment for us
     */
    @Test
    void thereIsAlwaysATimeZoneInActionInChangeToSavingTimeInEurope_Lisboa() {
        // On the night of Saturday 30 March 2019 to Sunday 31 March 2019
        // Daylight Saving Time begins on this day
        // Clocks must be advanced one hour from 00:00 to 01:00 hours.
        // One hour less = therefore one hour less sleep!

        // 26-03-2019T01:30 find the next SUNDAY

        final String timeZoneId = "Europe/Lisbon";
        final ZonedDateTime reference = YearMonth.of(2019, Month.MARCH).atDay(26).atTime(1, 30, 0).atZone(ZoneId.of(timeZoneId));

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        calendar.setTimeInMillis(reference.toInstant().toEpochMilli());

        do {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY);

        final Instant instant = Instant.ofEpochMilli(calendar.getTimeInMillis());
        final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(timeZoneId));

        /*
         * The day is correct ("2019-03-31"), the time is not exactly as expected :)
         */
        System.out.println(zonedDateTime);

        //"2019-03-31T00:30Z[Europe/Lisbon]"
        final ZonedDateTime expected = ZonedDateTime.of(2019, 03, 31, 0, 30, 0, 0, ZoneId.of(timeZoneId));

        Assertions.assertEquals(expected, zonedDateTime);
    }

    /**
     * When we call the {@link TimeZone#getTimeZone(String)} method passing an IANA identifier as a parameter, such as "Europe/Lisbon"
     * it returns an instance that contains all offsets history for this timezone. The question is:
     *
     * <pre>
     * Where does Java get these information?
     * </pre>
     * <p>
     * we already know that the IANA website contains all this informations.
     * But the {@link TimeZone#getTimeZone(String)} method does not fetch this information on the Internet.
     * In fact, when you install Java, it comes with an embedded version of TZDB inside the folder
     * JDK_HOME/jre/ lib or JRE_HOME/lib
     *
     * <pre>
     * And now I wait for new version of JDK to update the Timezones information or is there another way?
     * </pre>
     * <p>
     * Unfortunately, The JDK updates are not always as as fast as IANA's. In my opinion, it's understandable, after all, a new release of JDK will
     * not be released just for update TZDB. Generally the timezone update is just one of many items that are fixed or improved to
     * each new version.
     * <p>
     * <p>
     * Fortunately, we don't have to wait for a new version of JDK Just use the Timezone Updater Tool, or simply
     * TZUpdater.
     *
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tzupdater-readme-136440.html">There are instructions on its website to download and use</a>.
     * <p>
     * TZUpdater is basically a jar that you run with JVM you want to update by passing as parameters thw IANA file. Example:
     * <pre>
     *
     *  // Run theTZUpdater with the last version of fthe TZDB
     *  $java -jar tzupdater.jar -l https://www.iana.org/time-zones/repository/tzdata-latest.tar.gz
     *
     * </pre>
     * <p>
     * This option is the most common way to run as it updates the JVM to the most current version of TZDB.
     * You can also rotate it with the -h parameter, which shows all available options.
     * One is -V, which tells you which version of TZDB is in the JVM.
     * <p>
     * Note: When you are running TZUpdater, be sure to stop the JVM as recommended by Oracle FAQ
     *
     * @see <a href="https://www.iana.org/time-zones">IANA timezone more recents</a>
     * @see <a href="https://data.iana.org/time-zones/releases/">IANA timezones history</a>
     * @see <a href="http://www.oracle.com/technetwork/java/javase/documentation/tzupdater-readme-136440.html">TZUpdater</a>
     * @see <a href="http://www.oracle.com/technetwork/java/javase/dst-faq-138158.html#restart/">TZUpdater Oracle FAQ</a>
     */
    @Test
    void whereJavaTakesTimezoneInformation() {
    }

    /**
     * If I want the third monday of the current month
     */
    @Test
    void findThirdMondayOfTheMonth() {
        final String timeZoneId = "Europe/Lisbon";
        final ZonedDateTime reference = YearMonth.of(2019, Month.OCTOBER).atDay(15).atTime(1, 30, 0).atZone(ZoneId.of(timeZoneId));

        final Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        calendar.setTimeInMillis(reference.toInstant().toEpochMilli());

        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        final Instant instant = Instant.ofEpochMilli(calendar.getTimeInMillis());
        final ZonedDateTime thirdMondayOfTheMonth = instant.atZone(ZoneId.of(timeZoneId));

        //"2019-03-31T00:30Z[Europe/Lisbon]"
        final ZonedDateTime expected = ZonedDateTime.of(2019, 10, 21, 1, 30, 0, 0, ZoneId.of(timeZoneId));


        Assertions.assertEquals(expected, thirdMondayOfTheMonth);
    }
}
