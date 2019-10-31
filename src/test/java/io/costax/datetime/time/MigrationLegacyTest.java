package io.costax.datetime.time;

import io.costax.datetime.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class MigrationLegacyTest {

    /**
     * The Date is timestamp.
     */
    @Test
    void javaUtilDateTheBestMatch() {
        Date date = new Date();

        //	convert to Instant
        Instant instant = date.toInstant();

        //convert Instant back to Date
        date = Date.from(instant);

        Assertions.assertNotNull(date);
    }

    /**
     * Date has millisecond precision of (3 decimal places in the
     * seconds), while the precision of Instant is nanoseconds (9 decimal places).
     * When converting an Instant for Date, the value is truncated to milliseconds and the rest digits are lost. Example:
     */
    @Test
    void precisionLostFact() {
        Instant instant = Instant.parse("2018-01-01T10:00:00.123456789Z");

        System.out.println(instant.getNano());

        final Date from = Date.from(instant);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);

        final int i = calendar.get(Calendar.MILLISECOND);
        System.out.println(i);

        // back to instance
        final Instant instant1 = from.toInstant();
        System.out.println(instant1);

        System.out.println(instant1.getNano());

        Assertions.assertEquals(123_456_789, instant.getNano());
        Assertions.assertEquals(123_000_000, instant1.getNano());
    }

    /**
     * If we really need to convert back to instant with all 9 digits of nanoseconds we must save/preserve the nanoseconds singly
     */
    @Test
    void precisionLostSolution() {

        final Instant instantOriginal = Instant.parse("2018-01-01T10:00:00.123456789Z");
        final int instantOriginalNanos = instantOriginal.getNano();

        final Date from = Date.from(instantOriginal);

        //...
        final Instant restoredInstance = from.toInstant().with(ChronoField.NANO_OF_SECOND, instantOriginalNanos);

        Assertions.assertEquals(123_456_789, instantOriginal.getNano());
        Assertions.assertEquals(123_456_789, restoredInstance.getNano());
    }

    /**
     * How to convert {@link java.util.Date} to {@link LocalDate}
     * <p>
     * A very common UseCase is to convert a Date in LocalDate. And does not exists a direct way to do it.
     * Because an {@link java.util.Date} represent a timestamp so it represent different Date in each timezone.
     * We have to explicitly define the timezone.
     *
     * <pre>
     * The new API has been design to request explicitly one ZoneId, we can think that is obstacle.
     * But for me is a way to make us think in the best solution.
     * </pre>
     *
     * @see {@link ZoneId#systemDefault()}
     */
    @Test
    void convertDateToLocalDate() {
        final long l = Setup.clock().instant().toEpochMilli();

        final Date date = new Date(l);

        final LocalDate localDate =
                date.toInstant()
                        .atZone(ZoneId.of("Europe/Lisbon"))
                        .toLocalDate();

        Assertions.assertEquals("2018-05-04", localDate.toString());

        // convert back to a java.util.Date

        final Instant instantTemp = localDate.atTime(LocalTime.MIN).atZone(ZoneId.of("Europe/Lisbon")).toInstant();
        final Date from = Date.from(instantTemp);
    }


    /**
     * {@link Calendar} has a timestamp, but also has a timezone and its date and time fields
     * (day, month, year, hours, minutes, seconds) with the values adjusted according to your timezone.
     * <p>
     * Because it has a timestamp, this class also has the method toInstant(), which returns an Instant containing the same
     * timestamp value.
     * <p>
     * But there is no method to convert the  Instant back to {@link Calendar}.
     * <p>
     * An alternative is to set the timestamp numeric value directly:
     */
    @Test
    void convertUsingCalendar() {
        Instant now = Instant.now(Setup.clock());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now.toEpochMilli());


        Assertions.assertEquals(calendar.getTimeInMillis(), now.toEpochMilli());
    }

    /**
     * Calendar.getInstance() creates an instance with the JVM standard timezone,
     * <p>
     * so this is the timezone that the Calendar will use to get the date and time fields from the timestamp,
     * <p>
     * it is also possible pass a TimeZone to the getInstance() method so that the Calendar use another timezone.
     * <p>
     * <p>
     * Another detail is that the Calendar is abstract class and has several different implementations
     * (as there are several  different calendars in use in the world).
     * <p>
     * getInstance()  returns one of these implementations according to the default locale of the JVM.
     * <p>
     * For the the majority of locales, the return is a {@link java.util.GregorianCalendar}, which represents the calendar
     * Gregorian, which is currently used by most of the world.
     * <p>
     * The equivalent of {@link java.util.GregorianCalendar} is the class {@link ZonedDateTime}, since both represent a date and time in
     * given timezone.  That's why in Java 8 the  respective conversion methods.
     * <p>
     * The {@link GregorianCalendar#from(ZonedDateTime)} and {@link GregorianCalendar#toZonedDateTime()} methods preserve the
     * timezone and timestamp when converting between types.
     * <p>
     * With this both correspond to the same instant and to the same date, time and offset values.
     * <p>
     * Remember that when converting from  ZonedDateTime for GregorianCalendar, the nanoseconds are truncated to milliseconds.
     * As for the fact that these methods are not in Calendar, there is a brief discussion about it in the OpenJDK mailing list
     *
     * @see <a href="http://mail.openjdk.java.net/pipermail/threeten-dev/2013-June/001476.html/">Open JDK</a>
     */
    @Test
    void gregorianCalendar() {
        ZonedDateTime zonedDt = ZonedDateTime.now(Setup.clock());

        GregorianCalendar cal = GregorianCalendar.from(zonedDt);
        ZonedDateTime zdt = cal.toZonedDateTime();

        Assertions.assertEquals(zonedDt.toInstant(), zdt.toInstant());
    }

    @Test
    void zoneIdToTimeZone() {
        TimeZone.getTimeZone("IST").toZoneId();
        TimeZone.getTimeZone("EST").toZoneId();

        ZoneId zoneId = ZoneId.of("Asia/Tokyo");
        TimeZone tz = TimeZone.getTimeZone(zoneId);
    }


    /**
     * Date subclasses in the {@link java.sql} package also have conversion methods.
     * <pre>
     *
     *  {@link java.sql.Date} - has the {@link java.sql.Date#toLocalDate()} method, which returns a {@link LocalDate},
     *                          and {@link java.sql.Date#valueOf(LocalDate)} which converts {@link LocalDate} to java.sql.Date.
     *
     *
     * {@link java.sql.Time} - has the {@link Time#toLocalTime()} method, which returns a {@link LocalTime},
     *                         and {@link Time#valueOf(LocalTime)}, which converts LocalTime to Time.
     *
     *
     * {@link java.sql.Timestamp} - has the methods {@link java.sql.Timestamp#from(Instant)} and {@link Timestamp#toInstant()},
     *                              to convert from and to Instant, respectively.
     *                             Like Timestamp nanosecond precision, conversion is done without loss of precision.
     *                             In addition, this class also has Methods to convert to and from LocalDateTime:
     *                             {@link Timestamp#toLocalDateTime()} and {@link Timestamp#valueOf(LocalDateTime)}
     * </pre>
     * <p>
     * <p>
     * Conversions involving local types always use the JVM default timezone:
     * <p>
     * Remember that {@link java.sql.Date}, Time and Timestamp actually represent a timestamp
     * (for subclasses of java.util.Date) and their date values and time correspond to this timestamp in the standard JVM timezone.
     * We can see this behavior in the next example.
     * <p>
     * First a java.sql.Date was created with a value of  timestamp equivalent to 2018-05-04T17: 00+01:00.
     * <p>
     * After the  Default timezone is changed twice. The method toLocalDate() converts the timestamp value to timezone
     * default that is currently set, and this gets the value of the date that will be used in LocalDate.
     * <p>
     * Like the timestamp corresponds to May 4, 2018 in Lisbon, but to May 5 in Tokyo, the exit is.
     * <p>
     * The valueOf (LocalDate) method also suffers from this same problem.
     */
    @Test
    void javaSql() {
        //	timestamp	1525464000000	->	2018-05-04T17:00-03:00
        java.sql.Date date = new java.sql.Date(1525464000000L);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));

        System.out.println(date.toLocalDate());

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));

        System.out.println(date.toLocalDate());
    }
}
