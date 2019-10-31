package io.costax.datetime.legacy;

import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Function;

/**
 * The java.sql package contains several classes for working with databases.
 * <p>
 * All DataBase contains data types of Date and Time the 'java.sql.*' package contains the classes:
 * <p>
 * <pre>
 * - java.sql.Date:
 *      Represent an SQL DATE, therefore date (day, month and year)
 *
 * - java.sql.Time
 *      Represent an SQL TIME, therefore date (hour, minute, seconds)
 *
 * - java.sql.Timestamp
 *      Represent an SQL TIMESTAMP, it is the value of the timestamp (time elapsed since the Unix Epoch)
 *      but their meaning may be different between database implementations. eg. In Oracle for example we can find 3 timestamp:
 *
 *          - timestamp
 *          - timestamp with timezone
 *          - timestamp with local timezone
 *
 *      All this DataBase type are mapped to the {@link java.sql.Timestamp} class.
 * </pre>
 * <p>
 * All this classes are extensions of {@link java.util.Date}, do they inherit also their main feature: internally,
 * they have the value of the timestamp, which represent a point on the timeline, and so correspond to a different date depending on the timezone
 *
 *
 * <pre>
 *      It is import to know this classes because only since from JDBC 4.2  we can use the classes of the {@link java.time} to write and read date fields in a database Dice.
 * </pre>
 */
class JavaSQLPackageTest {

    private static final String EUROPE_LISBON = "Europe/Lisbon";
    private TimeZone defaultTimezone;

    @BeforeEach
    void setUp() {
        defaultTimezone = TimeZone.getDefault();
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(defaultTimezone);
    }

    /**
     * JAVA.SQL.DATE AND TIME NOT REPRESENT A DATE AND TIME SPECIFIC
     * <p>
     * Because they are subclasses of {@link java.util.Date}, the problems with this
     * class were also inherited by {@link java.sql.Date} and {@link java.sql.Time}. a
     * The main one is its toString () method, whose
     * affected by the standard JVM timezone as we can see
     * next example:
     * <p>
     * At least the toString() method returned the values in ISO 8601 format. This is an advance over java.util.Date.
     */
    @Test
    @DisplayName("11.1 - java.sql.Date AND java.sql.Time not represent a date and time specific")
    void dateAndTimeDoNotReallyRepresentsAnDateOrTime() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5).atDay(4).atTime(17, 0).atZone(ZoneId.of("Europe/Lisbon"));
        System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();
        java.sql.Date date = new java.sql.Date(instant.toEpochMilli());
        java.sql.Time time = new java.sql.Time(1525464000000L);

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));
        System.out.println(date);
        System.out.println(time);

        Assertions.assertEquals("2018-05-04", date.toString());
        Assertions.assertEquals("21:00:00", time.toString());

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        System.out.println(date);
        System.out.println(time);

        Assertions.assertEquals("2018-05-05", date.toString());
        Assertions.assertEquals("05:00:00", time.toString());
    }

    /**
     * In order to be able to represent a {@link java.sql.Date} even though  timestamp value, the java.sql.Date class hides artificially the time fields.
     * <p>
     * The methods that return these fields, like {@link Date#getHours()}, {@link Date#getMinutes()}, {@link Date#getSeconds()}
     * and its setters, launch a {@link IllegalArgumentException} when they are called.
     * <p>
     * However, it is possible to get the hours from a java.sql.Date, using a SimpleDateFormat.
     * <p>
     * So, even if you don't "have" the time, you can get it. These information is hidden but not so much. For a class that intended to represent only the date is not the best implementations.
     */
    @SuppressWarnings({"Convert2MethodRef", "ResultOfMethodCallIgnored"})
    @Test
    @DisplayName("11.2 - java.sql.Date hide the time information, but we can get it")
    void javaSqlDateHideTheTimeOnlyInTheToStringMethod() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5).atDay(4).atTime(17, 0).atZone(ZoneId.of(EUROPE_LISBON));
        System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();

        final Date sqlDate = new Date(instant.toEpochMilli());

        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.getHours());
        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.getMinutes());
        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.getSeconds());
        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.setHours(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.setMinutes(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sqlDate.setSeconds(1));

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", new Locale("pt", "PT"));
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        Assertions.assertEquals("2018-05-04T17:00:00+01:00", sdf.format(sqlDate));
    }

    /**
     * The Equals method consider the timestamp and not only the day-month-year, the same problem can be found in the java.sql.Time Class
     */
    @Test
    void equalsMethodIsBeaked() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5)
                .atDay(4)
                .atTime(17, 1, 2, 123456789)
                .atZone(ZoneId.of(EUROPE_LISBON));
        ///System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();
        long millis = instant.toEpochMilli();

        final Date sqlDate1 = new Date(millis);
        final Date sqlDate2 = new Date(millis + 1);

        System.out.println(sqlDate1);
        System.out.println(sqlDate2);
        System.out.println("Are Equals? " + sqlDate1.equals(sqlDate2));

        Assertions.assertEquals("2018-05-04", sqlDate1.toString());
        Assertions.assertEquals("2018-05-04", sqlDate2.toString());
        Assertions.assertNotEquals(sqlDate1, sqlDate2);
        Assertions.assertTrue(sqlDate1.before(sqlDate2));
        Assertions.assertTrue(sqlDate2.after(sqlDate1));
        Assertions.assertFalse(sqlDate1.compareTo(sqlDate2) == 0);
    }

    /**
     * setting the hours, minutes, seconds, and milliseconds to zero in the
     * particular time zone with which the instance is associated.
     */
    @Test
    void solvingEqualsMethodIsBeaked() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5)
                .atDay(4)
                .atTime(17, 1, 2, 123456789)
                .atZone(ZoneId.of(EUROPE_LISBON));
        ///System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();
        long millis = instant.toEpochMilli();

        final Function<java.sql.Date, java.sql.Date> normalizeSqlDates = sqlDate -> {
            final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(EUROPE_LISBON));
            cal.setLenient(false);
            cal.setTimeInMillis(sqlDate.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return new java.sql.Date(cal.getTimeInMillis());
        };

        final Date sqlDate1 = normalizeSqlDates.apply(new Date(millis));
        final Date sqlDate2 = normalizeSqlDates.apply(new Date(millis + 1));

        System.out.println(sqlDate1);
        System.out.println(sqlDate2);
        System.out.println("Are Equals? " + sqlDate1.equals(sqlDate2));

        Assertions.assertEquals("2018-05-04", sqlDate1.toString());
        Assertions.assertEquals("2018-05-04", sqlDate2.toString());
        Assertions.assertEquals(sqlDate1, sqlDate2);
        Assertions.assertFalse(sqlDate1.before(sqlDate2));
        Assertions.assertFalse(sqlDate2.after(sqlDate1));
        Assertions.assertTrue(sqlDate1.compareTo(sqlDate2) == 0);
    }

    /**
     * The class java.sql.Timestamp is the only one that seems to have a name consistent with its operation because it stores a timestamp.
     * But unfortunately it is also a subclass of java.util.Date, and so the same problems are inherited her.
     *
     * <pre>
     *     - The toString method also depends from JVM default TimeZone
     *     - The toString method do not show us any TimeZone or offset information
     *     - The toString method is not formatted in ISO 8601 format
     * </pre>
     */
    @Test
    @DisplayName("11.2 - java.sql.Timestamp, the only name what makes sense")
    void javaSqlTimestampIsAlsoAnSubclassOfJavaUtilDate() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5).atDay(4).atTime(17, 30, 45, 123456789).atZone(ZoneId.of(EUROPE_LISBON));
        ///System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();
        long millis = instant.toEpochMilli();

        final Timestamp timestamp = new Timestamp(millis);

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        System.out.println(timestamp);
        Assertions.assertEquals("2018-05-05 01:30:45.123", timestamp.toString());

        TimeZone.setDefault(TimeZone.getTimeZone(EUROPE_LISBON));
        System.out.println(timestamp);
        Assertions.assertEquals("2018-05-04 17:30:45.123", timestamp.toString());
    }

    /**
     * The {@link java.sql.Timestamp} is a subclass of {@link java.util.Date} But not everything is bad news:
     *
     * <pre>
     *    - Unlike Date and other classes, which have precision  milliseconds (3 decimal places), the Timestamp class has nanosecond accuracy (9 decimal places)
     * </pre>
     */
    @Test
    void javaSqlTimestampHaveBetterPrecision() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2018, 5).atDay(4).atTime(17, 30, 45, 123456789).atZone(ZoneId.of(EUROPE_LISBON));
        ///System.out.println(zonedDateTime);
        final Instant instant = zonedDateTime.toInstant();
        long millis = instant.toEpochMilli();

        final Timestamp ts = new Timestamp(millis);
        System.out.println(ts.getTime());
        System.out.println(ts.getNanos());

        Assertions.assertEquals(1525451445123L, ts.getTime());
        Assertions.assertEquals(123000000, ts.getNanos());
    }
}
