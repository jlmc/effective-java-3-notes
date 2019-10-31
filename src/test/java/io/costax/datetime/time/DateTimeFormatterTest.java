package io.costax.datetime.time;

import io.costax.datetime.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 17. USING PATTERN AND LOCALES
 *
 * @see <a href="https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/time/format/DateTimeFormatter.html">DateTimeFormatter</a>
 */
class DateTimeFormatterTest {

    /**
     * The simplest way to create a  dateTimeFormatter instance is by using a Pattern as a parameter.
     * <p>
     * <b>We must pay attention to the pattern</b>
     * <p>
     * The pattern is not the same that we would use with {@link java.text.SimpleDateFormat} to get the date in this
     * Format. We must be careful when migrating our code to the new API.
     * Many letters of the pattern are the same in both {@link DateTimeFormatter} and {@link java.text.SimpleDateFormat} but not all of them work like this.
     * Some work the other way,
     * others correspond to different fields, and several new patterns were added in {@link java.time}. Always consult the
     * documentation to see what each letter means and what its operation.
     * <p>
     * E.g. The character 'u', corresponds to the day of the week in the legacy API, but in {@link java.time} is the year
     *
     * </p>
     *
     * @see <a href="https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/time/format/DateTimeFormatter.html">DateTimeFormatter</a>
     */
    @Test
    void dateTimeFormatterFromPattern() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 22),
                LocalTime.of(21, 39),
                ZoneId.of("Europe/Lisbon"));

        final DateTimeFormatter dateTimeFormatterDefault = DateTimeFormatter.ofPattern("yyyy-MMMM-dd");
        final DateTimeFormatter dateTimeFormatterPT = DateTimeFormatter.ofPattern("uuuu-MMMM-dd", new Locale("pt", "PT"));
        final DateTimeFormatter dateTimeFormatterUS = DateTimeFormatter.ofPattern("uuuu-MMMM-dd", new Locale("en", "US"));

        System.out.println(dateTimeFormatterDefault.format(zonedDateTime));
        System.out.println(dateTimeFormatterPT.format(zonedDateTime));
        System.out.println(dateTimeFormatterUS.format(zonedDateTime));

        Assertions.assertEquals("2019-outubro-22", dateTimeFormatterPT.format(zonedDateTime));
        Assertions.assertEquals("2019-October-22", dateTimeFormatterUS.format(zonedDateTime));
    }

    /**
     * The year may be represented in two ways in the new API:
     * <p>
     * there is the field <b>year-of-age</b>, represented by the letter 'y' and {@link java.time.temporal.ChronoField#YEAR_OF_ERA}
     * , and the year field, represented by
     * Letter 'u' by {@link java.time.temporal.ChronoField#YEAR}.
     * <p>
     * The difference is in the way negative years are treated (or "AC" - the popular "Before Christ"), as we can see in the next example.
     * <p>
     * <p>
     * This difference happens because y considers that there was no year zero:
     * the first year of the era we are currently in is the  "year 1 after Christ", and the year before this is "year 1 before Christ ".
     * <p>
     * Pattern u considers that before year 1 comes the year zero and before that the years are negative. In short:
     * <p>
     * <table style>
     * <col width="25%"/>
     * <col width="25%"/>
     * <thead>
     * <tr><th>y and G</th>
     * <th>  u  </th>
     * </tr>
     * </thead>
     * <tbody>
     *
     * <tr><td>2 DC</td><td>2</td></tr>
     * <tr><td>1 DC</td><td>1</td></tr>
     * <tr><td>1 AC</td><td>0</td> </tr>
     * <tr><td>2 AC</td><td>-1</td></tr>
     * <tr><td>3 AC</td><td>-2</td></tr>
     * </tbody>
     * </table>
     * <p>
     * <p>
     * Therefore, the year -10 is printed as -0010 for the pattern. uuuu and 0011 for the yyyy pattern.
     * But the value 0011 is ambiguous as it can be either 11 BC or 11.
     */
    @Test
    void negativeYears() {
        final DateTimeFormatter yyyyMMMMdd = DateTimeFormatter.ofPattern("yyyy-MMMM-dd");
        final DateTimeFormatter uuuuMMMMdd = DateTimeFormatter.ofPattern("uuuu-MMMM-dd");

        /*
         * Pattern G was also used, which corresponds to the "Age" field (indicating the ages before and after Christ)
         */
        final DateTimeFormatter mixed = DateTimeFormatter.ofPattern("uuuu yyyy GG GG", new Locale("pt", "PT"));


        LocalDate date = LocalDate.of(-10, 1, 1);

        System.out.println(date.format(yyyyMMMMdd));
        System.out.println(date.format(uuuuMMMMdd));
        System.out.println(date.format(mixed));

        Assertions.assertEquals("0011-January-01", date.format(yyyyMMMMdd));
        Assertions.assertEquals("-0010-January-01", date.format(uuuuMMMMdd));


        Assertions.assertEquals("-0010 0011 a.C. a.C.", date.format(mixed));
    }

    @Test
    void unformattable() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        final LocalDate date = LocalDate.of(2019, Month.OCTOBER, 25);

        final UnsupportedTemporalTypeException unsupportedTemporalTypeException = Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> date.format(dateTimeFormatter));
        Assertions.assertEquals("Unsupported field: HourOfDay", unsupportedTemporalTypeException.getMessage());
    }

    @Test
    void formatInstant() {
        final Instant instant = Instant.now(Setup.clock());
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm");

        /*
         * This is because Instant represents a timestamp,
         * therefore it may correspond to a different date and time in
         * Timezone But the DateTimeFormatter is created without a
         * timezone (as opposed to SimpleDateFormat, which always uses
         * JVM standard) and so it has no way of getting the values of
         * date and time fields.
         *
         * To format Instant, we must first convert it to some timezone or offset using atZone() and atOffset() as previously seen.
         * Then, we can pass the result (ZonedDateTime or OffsetDateTime) to the DateTimeFormatter.
         */
        final UnsupportedTemporalTypeException unsupportedTemporalTypeException = Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> dateTimeFormatter.format(instant));
        Assertions.assertEquals("Unsupported field: DayOfMonth", unsupportedTemporalTypeException.getMessage());

        /*
         * timezone Europe/Lisbon was set in DateTimeFormatter.
         * When calling the format() method, Instant is converted to this timezone.
         * This only happens with objects that can be converted to Instant (such as ZonedDateTime and OffsetDateTime).
         * The local classes, for example, will not be affected by this setting.
         */
        final String format = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm").withZone(ZoneId.of("Europe/Lisbon")).format(instant);
        Assertions.assertNotNull(format);

        final LocalDate localDate = LocalDate.of(2017, 1, 1);
        Assertions.assertEquals("01-01-2017 00:00",
                DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm").withZone(ZoneId.of("Europe/Lisbon")).format(localDate.atTime(LocalTime.MIN)));
    }

    // Format to ISO-8601

    /**
     * With {@link java.time} formatting for ISO-8601 is more simple because there are already several predefined formatters.
     * They are all static constants of the DateTimeFormatter class, as the next example shows:
     */
    @Test
    void formatToIso8601() {
        //2019-10-26T16:15:30.123456789+01:00[Europe/Lisbon]
        ZonedDateTime now = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 26),
                LocalTime.of(16, 15, 30, 123_456_789),
                ZoneId.of("Europe/Lisbon"));

        Assertions.assertEquals("2019-10-26", DateTimeFormatter.ISO_LOCAL_DATE.format(now));
        Assertions.assertEquals("2019-10-26T16:15:30.123456789", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now));
        Assertions.assertEquals("2019-10-26T16:15:30.123456789+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now));
    }

    /**
     * ISO-8601 allows an Offset to be represented with the colon (-03:00), without the colon (-0300) or only with the hour value (-03).
     * <p>
     * The predefined formatters always use the colon format. To have the offset in a different format, we can use the pattern 'X' or 'x',
     * whose result depends on the number of letters:
     * </p>
     *
     * <pre>
     *
     * - XXX or xxx: Offset with the colon (-03:00)
     * - XX or xx: Offset without the colon (-0300)
     * - X or x: only with the hour value (-03)
     *
     * </pre>
     * <p>
     * The difference between Uppercase X and Lowercase x is the way how they treat Zero Offset.
     * <pre>
     * - The Uppercase X return the zero offset as 'Z' (in accordance with the definition of ISO 8601 to designate UTC).
     * - The Lowercase x return the zero offset as +00:00, +0000 or +00 depending on the amount of letters.
     * </pre
     */
    @Test
    void howToChangeTheOffsetFormatFormat() {
        //2019-10-26T16:15:30.123456789+01:00[Europe/Lisbon]
        ZonedDateTime now = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 26),
                LocalTime.of(16, 15, 30, 123_456_789),
                ZoneId.of("Europe/Lisbon"));

        //2019-10-27T16:15:30.123456789Z[Europe/Lisbon]
        ZonedDateTime tomorrow = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 27),
                LocalTime.of(16, 15, 30, 123_456_789),
                ZoneId.of("Europe/Lisbon"));

        // Using Uppercase X

        final DateTimeFormatter isoDateTimeFormaterWithColon = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX");
        Assertions.assertEquals("2019-10-26T16:15:30+01:00", isoDateTimeFormaterWithColon.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30Z", isoDateTimeFormaterWithColon.format(tomorrow));

        final DateTimeFormatter isoDateTimeFormaterWithoutColon = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXX");
        Assertions.assertEquals("2019-10-26T16:15:30+0100", isoDateTimeFormaterWithoutColon.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30Z", isoDateTimeFormaterWithoutColon.format(tomorrow));

        final DateTimeFormatter isoDateTimeFormaterWithoutOnlyHours = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
        Assertions.assertEquals("2019-10-26T16:15:30+01", isoDateTimeFormaterWithoutOnlyHours.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30Z", isoDateTimeFormaterWithoutOnlyHours.format(tomorrow));

        // Using Lowercase X

        final DateTimeFormatter isoDateTimeFormaterWithColonx = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxx");
        Assertions.assertEquals("2019-10-26T16:15:30+01:00", isoDateTimeFormaterWithColonx.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30+00:00", isoDateTimeFormaterWithColonx.format(tomorrow));


        final DateTimeFormatter isoDateTimeFormaterWithoutColonx = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx");
        Assertions.assertEquals("2019-10-26T16:15:30+0100", isoDateTimeFormaterWithoutColonx.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30+0000", isoDateTimeFormaterWithoutColonx.format(tomorrow));

        final DateTimeFormatter isoDateTimeFormaterWithoutOnlyHoursx = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssx");
        Assertions.assertEquals("2019-10-26T16:15:30+01", isoDateTimeFormaterWithoutOnlyHoursx.format(now));
        Assertions.assertEquals("2019-10-27T16:15:30+00", isoDateTimeFormaterWithoutOnlyHoursx.format(tomorrow));
    }

    /**
     * Optional values in the patterns
     * <p>
     * A new feature introduced by {@link DateTimeFormatter} is the possibility of using optional patterns.
     * </p>
     * <p>
     * <p>
     * With this, the fields only will be formatted if available. The optional pattern is bracketed, e.g:
     * <code>
     * DateTimeFormatter.ofPattern("dd-MM-uuuu[ HH:mm:ss]");
     * </code>
     * </p>
     * <p>
     * The pattern has the day, month and year (dd-MM-uuuu), which always will be required
     * (if the object being formatted does not have any of these fields, an exception will be thrown).
     * <p>
     * Then the square brackets delimit an optional section: a blank space followed by hour, minute and second ([ HH:mm:ss])
     * - don't ignore spaces, they are also part of the pattern and are considered in formatting.
     * <p>
     * Fields within the optional section are printed only if the object being formatted has them.
     * How {@link LocalDate} does not have time fields when formatting it the optional section is ignored,
     * If fields were not optional, an exception would be thrown). An {@link LocalDateTime} has a time, so the optional section is used.
     */
    @Test
    void optionalPatterns() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu[ HH:mm:ss[:xxx]]");

        final LocalDate localDate = LocalDate.of(2019, Month.JANUARY, 1);
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(8, 30, 45, 123_000_000));
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Europe/Lisbon"));

        Assertions.assertEquals("01-01-2019", localDate.format(formatter));
        Assertions.assertEquals("01-01-2020 08:30:45", localDateTime.format(formatter));
        Assertions.assertEquals("01-01-2020 08:30:45:+00:00", zonedDateTime.format(formatter));
    }

    @Test
    void advancedOptions() {
        final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // hour:minute:seconds
                .appendPattern("HH:mm:ss")
                // nanoseconds,	with 6 decimal digits max
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true)
                .toFormatter();

        LocalTime time = LocalTime.of(18, 51, 14, 123_456_789);

        Assertions.assertEquals("18:51:14.123456", time.format(formatter));
    }

    @Test
    void advancedFormatter() {

        final Map<Long, String> integerStringMap = Map.of(
                1L, "Seg.",
                2L, "Ter",
                3L, "Qua",
                4L, "Qui",
                5L, "Sex",
                6L, "Sab",
                7L, "Dom");

        ZonedDateTime date = ZonedDateTime.of(
                LocalDate.of(2019, Month.OCTOBER, 26),
                LocalTime.of(19, 25, 30, 123_456_789),
                ZoneId.of("Europe/Lisbon"));

        final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                //.appendText(ChronoField.DAY_OF_WEEK, integerStringMap)
                .appendPattern("W EEEE 'de' MMMM")
                .toFormatter(new Locale("pt"));

        Assertions.assertEquals("4 sábado de outubro", formatter.format(date));

        final DateTimeFormatter formatter2 = new DateTimeFormatterBuilder()
                .appendPattern("W ")
                .appendText(ChronoField.DAY_OF_WEEK, integerStringMap)
                .appendPattern(" 'de' MMMM")
                .toFormatter(new Locale("pt"));

        Assertions.assertEquals("4 Sab de outubro", formatter2.format(date));


        final DateTimeFormatter formatter3 = new DateTimeFormatterBuilder()
                .appendText(ChronoField.ALIGNED_WEEK_OF_MONTH,
                        Map.of(1L, "Primeiro",
                                2L, "Segundo",
                                3L, "Terceiro",
                                4L, "Quarto",
                                5L, "Quinto",
                                6L, "Quinto",
                                7L, "Setimo"
                        )
                )
                .appendLiteral(' ')
                .appendText(ChronoField.DAY_OF_WEEK, integerStringMap)
                .appendPattern(" 'de' MMMM")
                .toFormatter(new Locale("pt"));

        Assertions.assertEquals("Quarto Sab de outubro", formatter3.format(date));
    }

    @Test
    void parsing() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("uuuu-MM-dd['T'HH:mm[:xxx]]");

        final TemporalAccessor temporalAccessor = parser.parse("2019-01-02");

        Assertions.assertEquals(2019, temporalAccessor.get(ChronoField.YEAR));
        Assertions.assertEquals(LocalDate.of(2019, 1, 2), temporalAccessor.query(LocalDate::from));
        Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> temporalAccessor.get(ChronoField.HOUR_OF_DAY));


        final TemporalAccessor parse = parser.parse("2019-01-02T19:59:+01:00");

        Assertions.assertEquals(2019, parse.get(ChronoField.YEAR));
        Assertions.assertNotNull(parse.query(LocalDateTime::from));
        Assertions.assertEquals(19, parse.get(ChronoField.HOUR_OF_DAY));
        Assertions.assertNotNull(parse.query(OffsetDateTime::from));
    }

    /**
     * In the next example, I use a pattern where the date is required and the time is optional.
     * <p>
     * Then I define which value will be  used for the time when it is not present, using the parseDefaulting() method.
     * <p>
     * Finally, I also use this method To set the offset value:
     */
    @Test
    void buildingAParser() {

        final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("uuuu-MM-dd[ HH:mm]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 10)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 5)
                .parseDefaulting(ChronoField.OFFSET_SECONDS, ZoneOffset.ofHours(1).getTotalSeconds())
                .toFormatter();

        final TemporalAccessor parsed = formatter.parse("2019-11-11");

        Assertions.assertEquals(10, parsed.get(ChronoField.HOUR_OF_DAY));
        Assertions.assertEquals(5, parsed.get(ChronoField.MINUTE_OF_HOUR));
    }

    /**
     * We can parse two String using optional pattern:
     * <code>
     * DateTimeFormatter parser = DateTimeFormatter.ofPattern("[dd/MM/uuuu][uuuu-MM-dd]");
     * </code>
     * <p>
     * However, the example DateTimeFormatter is not good for formatting, because for each optional section it is checked
     * that the object contains all the fields in the section.
     * <p>
     * How both use the day, month, and year fields (exactly what LocalDate has), the two optional sections
     * are used and the date is printed in both formats.
     * That is, if if we do data.format (parser) the result will be 05/05/20182018-05-04.
     * <p>
     * To avoid this problem, it is best to use another DateTimeFormatter to format the date.
     */
    @Test
    void acceptbBothPatterns() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("[dd/MM/uuuu][uuuu-MM-dd]");

        LocalDate data1 = LocalDate.parse("2018-05-04", parser);
        LocalDate data2 = LocalDate.parse("04/05/2018", parser);

        Assertions.assertNotNull(data1);
        Assertions.assertNotNull(data2);
    }

    /**
     * By default, date and time classes can parse from Strings in ISO 8601 format, without needing a
     * DateTimeFormatter.
     * <p>
     * The "exception" is {@link ZonedDateTime} because as the ISO 8601 standard does not accept IANA identifiers,
     * this class has extended the format adding the timezone name in square brackets,
     * but the others Fields (date, time and offset) are in accordance with ISO 8601.
     * <p>
     * Internally, each one of this classes uses one of the predefined constants in the {@link DateTimeFormatter} class.
     * {@link LocalDate}, for example, uses {@link DateTimeFormatter#ISO_LOCAL_DATE},
     * ie your method parse() will only accept strings in the format "uuuu-MM-dd".
     * See the documentation of each class to know which format each accepted.
     */
    @Test
    void parsingISO18Format() {
        final LocalDate localDate = LocalDate.parse("2019-10-26");
        final LocalDateTime localDateTime = LocalDateTime.parse("2019-10-26T22:35:18.226854");
        final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2019-10-26T22:35:18.226854+01:00");
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse("2019-10-26T22:35:18.226854+01:00[Europe/Lisbon]");

        Assertions.assertNotNull(localDate);
        Assertions.assertNotNull(localDateTime);
        Assertions.assertNotNull(offsetDateTime);
        Assertions.assertNotNull(zonedDateTime);
    }

    /**
     * If you need to parse a different format, create a specific DateTimeFormatter and pass it as a parameter in parse() method.
     */
    @Test
    void parsingDifferentFormats() {
        DateTimeFormatter parser = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                //	offset:	-03:00,	-0300	ou	-03
                .appendPattern("[XXX][XX][X]")
                .appendPattern("['['v']']")
                .toFormatter();

        System.out.println(OffsetDateTime.parse("2018-05-04T10:00-03", parser));
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse("2019-10-26T10:00+01:00[Europe/Lisbon]", parser);
        System.out.println(zonedDateTime);
        final ZoneId zone = zonedDateTime.getZone();
        Assertions.assertEquals("Europe/Lisbon", zone.getId());
    }

    /**
     * The timezone abbreviations (such as "BRT", "IST", "EST" etc.) are not considered Timezones.
     * Also, many are ambiguous, like "IST", which is used in India, Israel and Ireland.
     * <p>
     * So when parsing from  an abbreviation, we must pay attention to this fact.
     * The problem is that DateTimeFormatter uses some arbitrary timezone when parsing an abbreviation and will not always be what you need.
     * For example:
     */
    @Test
    void parsingOfAbbreviationOfTimezone() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z uuuu", Locale.ENGLISH);

        System.out.println(ZonedDateTime.parse("Sun Jan 07 10:00:00 EST 2018", parser));

        /*
         * The input String has the abbreviation "EST", but what was timezone used, that of:
         * - America/New_York
         * - America/Atikokan
         * - America/Cayman
         * - America/Jamaica
         * - America/Panama
         *
         * The DateTimeFormatter Parser must pick one of these timezones in random way.
         *
         * If we want the abbreviation to match another timezone, we must setup a set of timezones to use in case of ambiguity,
         * using a java.util.Set with the ZoneIds and passing it to a DateTimeFormatterBuilder.
         */

        // conflict resolutions
        Set<ZoneId> zones = new HashSet<>();
        zones.add(ZoneId.of("America/Jamaica"));
        zones.add(ZoneId.of("Asia/Shanghai"));
        zones.add(ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter parser2 = new DateTimeFormatterBuilder()
                .appendPattern("EEE MMM dd HH:mm:ss ")
                //	use	java.time.format.TextStyle for the timezone Abbreviation, note that we are defined the conflict resolutions
                .appendZoneText(TextStyle.SHORT, zones)
                .appendPattern(" uuuu").toFormatter(Locale.ENGLISH);

        parser2.withResolverStyle(ResolverStyle.LENIENT);

        final ZonedDateTime zonedDateTimeInJamaica = ZonedDateTime.parse("Sun Jan 07 10:00:00 EST 2018", parser2);
        Assertions.assertEquals("America/Jamaica", zonedDateTimeInJamaica.getZone().getId());
        Assertions.assertEquals("Asia/Shanghai", ZonedDateTime.parse("Sun Jan 07 10:00:00 CST 2018", parser2).getZone().getId());
    }
}