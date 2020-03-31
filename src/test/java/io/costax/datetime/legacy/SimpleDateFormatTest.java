package io.costax.datetime.legacy;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

class SimpleDateFormatTest {

    private static final String EUROPE_LISBON = "Europe/Lisbon";
    private static final String ASIA_TOKYO = "Asia/Tokyo";
    private static final String AMERICA_NEW_YORK = "America/New_York";

    private static final Locale LOCALE_PORTUGAL = new Locale("pt", "PT");
    private static final Locale LOCALE_JAPAN = Locale.JAPAN;
    private static final Locale LOCALE_US = Locale.US;
    private static final Locale LOCALE_FRANCE = new Locale("fr", "FR");

    /**
     * - DateFormat is used for formatting a date into String based on specific locale that is provided as input.
     * <p>
     * - The locale is used for specifying the region and language for making the code more locale to the user.
     * <p>
     * - The way of writing date is different in different regions of the world.
     * For example, 31st Dec 2017 will be written in India as 31-12-2017 but the same thing will be written in US as 12-31-2017.
     * <p>
     * - Date Format classes are not synchronized, it’s recommended to create separate instance for each thread.
     * <p>
     * <p>
     * - {@link DateFormat} object can be created using the {@link DateFormat#getDateInstance()}
     * and {@link DateFormat#getTimeInstance()} method of the {@link DateFormat} class.
     */
    @Test
    void javaDateFormat() {

        final Instant instant = YearMonth.of(2019, Month.OCTOBER)
                .atDay(12)
                .atTime(14, 25)
                .atZone(ZoneId.of(EUROPE_LISBON))
                .toInstant();
        final Date referenceDate = Date.from(instant);

        DateFormat dateFormatPT = DateFormat.getDateInstance(DateFormat.FULL, LOCALE_PORTUGAL);
        DateFormat dateFormatJP = DateFormat.getDateInstance(DateFormat.FULL, LOCALE_JAPAN);
        DateFormat dateFormatUS = DateFormat.getDateInstance(DateFormat.FULL, LOCALE_US);

        assertThat(dateFormatPT.format(referenceDate), is("sábado, 12 de outubro de 2019"));
        assertThat(dateFormatJP.format(referenceDate), is("2019年10月12日土曜日"));
        assertThat(dateFormatUS.format(referenceDate), is("Saturday, October 12, 2019"));

        assertThat(DateFormat.getDateInstance(DateFormat.DEFAULT, LOCALE_PORTUGAL).format(referenceDate),
                is("12/10/2019"));
        assertThat(DateFormat.getDateInstance(DateFormat.DEFAULT, LOCALE_JAPAN).format(referenceDate),
                is("2019/10/12"));
        assertThat(DateFormat.getDateInstance(DateFormat.DEFAULT, LOCALE_US).format(referenceDate),
                is("Oct 12, 2019"));
    }

    /**
     * For performing a time format, we need an instance of time. We will be using getTimeInstance() method for getting an instance of time.
     */
    @Test
    void javaTimeFormat() {
        DateFormat timeFormatFR = DateFormat.getTimeInstance(DateFormat.DEFAULT, LOCALE_FRANCE);
        DateFormat timeFormatUS = DateFormat.getTimeInstance(DateFormat.DEFAULT, LOCALE_US);
        DateFormat timeFormatJP = DateFormat.getTimeInstance(DateFormat.DEFAULT, LOCALE_JAPAN);
        DateFormat timeFormatPT = DateFormat.getTimeInstance(DateFormat.DEFAULT, LOCALE_PORTUGAL);

        final Instant instant = YearMonth.of(2019, Month.OCTOBER)
                .atDay(12)
                .atTime(14, 25, 15)
                .atZone(ZoneId.of(EUROPE_LISBON))
                .toInstant();
        final Date referenceDate = Date.from(instant);

        Assert.assertEquals("14:25:15", timeFormatFR.format(referenceDate));
        Assert.assertEquals("2:25:15 PM", timeFormatUS.format(referenceDate));
        Assert.assertEquals("14:25:15", timeFormatJP.format(referenceDate));
        Assert.assertEquals("14:25:15", timeFormatPT.format(referenceDate));

        Assert.assertEquals("14:25:15 heure d’été d’Europe de l’Ouest", DateFormat.getTimeInstance(DateFormat.FULL, LOCALE_FRANCE).format(referenceDate));
        Assert.assertEquals("2:25:15 PM Western European Summer Time", DateFormat.getTimeInstance(DateFormat.FULL, LOCALE_US).format(referenceDate));
        Assert.assertEquals("14時25分15秒 西ヨーロッパ夏時間", DateFormat.getTimeInstance(DateFormat.FULL, LOCALE_JAPAN).format(referenceDate));
        Assert.assertEquals("14:25:15 Hora de verão da Europa Ocidental", DateFormat.getTimeInstance(DateFormat.FULL, LOCALE_PORTUGAL).format(referenceDate));
    }

    /**
     * SimpleDateFormat is very much like DateFormat,
     * the only major difference between them is that SimpleDateFormat can be used for:
     * formatting (Date to String conversion) and for parsing (String to Date conversion) both,
     * whereas DateFormat allows only formatting of Date.
     * <p>
     * The {@link java.text.SimpleDateFormat} use by default the Timezone of the JVM, this means that if we change the time zone
     * the result could be different.
     * <p>
     * <p>
     * Is very common need to format some {@link Date } for a String with the weak day name or the name of the month.
     * For the Month name we can use 3 or 4 chars 'M',
     * For the Weak day name we can use 3 or 4 chars 'E',
     */
    @Test
    void formatDate() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atTime(22, 9)
                .atZone(ZoneId.of(EUROPE_LISBON));
        final Date someDate = Date.from(zonedDateTime.toInstant());


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final String format = formatter.format(someDate);
        Assertions.assertEquals("09/10/2019", formatter.format(someDate));

        final SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE dd-MMMMM-yyyy", LOCALE_PORTUGAL);
        formatter2.setTimeZone(TimeZone.getTimeZone(ZoneId.of(EUROPE_LISBON)));
        Assertions.assertEquals("quarta-feira 09-outubro-2019", formatter2.format(someDate));

        final SimpleDateFormat formatter3 = new SimpleDateFormat("EEE dd-MMM-yyyy", LOCALE_PORTUGAL);
        formatter3.setTimeZone(TimeZone.getTimeZone(ZoneId.of(EUROPE_LISBON)));
        Assertions.assertEquals("quarta 09-out.-2019", formatter3.format(someDate));
    }

    @Test
    void parseDate() throws ParseException {
        final String pattern = "dd-MM-yyyy";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        final Date parsed = simpleDateFormat.parse("09-10-2019");

        Assert.assertEquals(YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atStartOfDay()
                .atZone(ZoneId.of(EUROPE_LISBON)).toInstant().toEpochMilli(), parsed.getTime());
    }

    @Test
    void simpleDateFormatWithLocale() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atTime(22, 9)
                .atZone(ZoneId.of("Europe/Lisbon"));
        final Date someDate = Date.from(zonedDateTime.toInstant());

        String pattern = "EEEEE MMMMM yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, LOCALE_FRANCE);

        Assert.assertEquals("mercredi octobre 2019 22:09:00.000+0100", simpleDateFormat.format(someDate));
    }

    /**
     * The {@link java.text.SimpleDateFormat} use by dafault the JVM timeZone.
     * If we want to use a different TimeZone we should use the method {@link SimpleDateFormat#setTimeZone(TimeZone)}
     * <p>
     * <p>
     * As we know that an instance of {@link java.util.Date} doesn't have TimeZone,
     * it's just an epoch time (in milliseconds) that represents different Date and time for different timezones.
     * <p>
     * In the next example we can see how a the same date is formatted differently using different TimeZones in the {@link SimpleDateFormat}.
     */
    @Test
    void simpleDateFormatterWithOtherTimeZone() {
        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atTime(0, 5, 45)
                .atZone(ZoneId.of(EUROPE_LISBON));
        final Date someDate = Date.from(zonedDateTime.toInstant());

        final String pattern = "dd-MM-yyyy HH:mm:ss";

        SimpleDateFormat formatterAsiaTokyo = new SimpleDateFormat(pattern);
        formatterAsiaTokyo.setTimeZone(TimeZone.getTimeZone(ASIA_TOKYO));

        SimpleDateFormat formatterEuropeLisbon = new SimpleDateFormat(pattern);
        formatterEuropeLisbon.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        SimpleDateFormat formatterAmericaNewYork = new SimpleDateFormat(pattern);
        formatterAmericaNewYork.setTimeZone(TimeZone.getTimeZone(AMERICA_NEW_YORK));

        Assert.assertEquals("09-10-2019 08:05:45", formatterAsiaTokyo.format(someDate));
        Assert.assertEquals("09-10-2019 00:05:45", formatterEuropeLisbon.format(someDate));
        Assert.assertEquals("08-10-2019 19:05:45", formatterAmericaNewYork.format(someDate));
    }

    @Test
    void format_to_iso_8601() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssXXX"), "Illegal pattern	character 'T'");

        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atTime(0, 5, 45)
                .atZone(ZoneId.of(EUROPE_LISBON));
        final Date someDateSummerDay = Date.from(zonedDateTime.toInstant());

        final ZonedDateTime zonedDateTime2 = YearMonth.of(2020, Month.JANUARY)
                .atDay(1)
                .atTime(0, 5, 45)
                .atZone(ZoneId.of(EUROPE_LISBON));
        final Date someDateWinterDay = Date.from(zonedDateTime2.toInstant());

        /*
         * XXX:	imprime	o offset com dois pontos. Ex.: -03:00	.
         * XX:	imprime	o offset sem os	dois pontos. Ex.: -0300	.
         * X:	imprime	o offset, mas somente o	valor das horas. Ex.: -03	.
         */
        final SimpleDateFormat simpleDateFormatPT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSXXX", LOCALE_PORTUGAL);
        simpleDateFormatPT.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        Assertions.assertEquals("2019-10-09T00:05:45:000+01:00", simpleDateFormatPT.format(someDateSummerDay));
        Assertions.assertEquals("2020-01-01T00:05:45:000Z", simpleDateFormatPT.format(someDateWinterDay));
    }


    /* ************* Parsing ******************/

    /**
     * PARSING: CONVERTING TEXT TO {@link Date}
     * <p>
     * To parse (convert a text to a date), you
     * You need to tell which format the text is in. But there are several
     * details that you should pay attention to when doing so. We will
     * suppose I have a Date with the current date which was next
     * formatted to a String:
     */
    @Test
    void simpleParsing() throws ParseException {

        final ZonedDateTime zonedDateTime = YearMonth.of(2019, Month.OCTOBER)
                .atDay(9)
                .atTime(0, 5, 45, 0)
                .atZone(ZoneId.of(EUROPE_LISBON));

        final Date someDateSummerDay = Date.from(zonedDateTime.toInstant());

        Assertions.assertEquals(someDateSummerDay.getTime(), zonedDateTime.toInstant().toEpochMilli());
        // using a formatter day-month-year
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", LOCALE_PORTUGAL);
        formatter.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        // 09-10-2019
        final String formatted = formatter.format(someDateSummerDay);

        // ------

        // now, parsing back the text to Date
        final String txt = "09-10-2019";

        /*
         * As we can see, the result of PARSING is not the same instant (epoch time) as the original (the one that was formatted)
         *
         * This is because the String that was passed to the method parse() only has day, month and year.
         * Although this String is the result of formatting a Date with a timestamp value specific,
         * it does not have the complete information (date, time and offset / timezone).
         *
         *  String does not know that it is the result of formatting a Date, nor that the timestamp value was 1570575945000.
         * She only knows that her value is "09-10-2019"
         * but It has no information about the context in which it was generated.
         *
         * Therefore, the parse() method will only work with the information you have to return a Date.
         * The String of entry has a day equal to 9, month 10 and year 2019 (values obtained at match 09-10-2019 with the pattern dd-MM-yyyy).
         *
         * And having only one day, month and year, it's not possible obtain a single timestamp.
         *
         * Therefore, SimpleDateFormat fills in the information that missing using default values.
         * For hours, this value is midnight (00:00:00). The standard JVM timezone to know what offset that day and time.
         * Gathering all the information gives the timestamp, which is used to build Date.
         *
         **/
        final SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
        final Date parsed = parser.parse(txt);
        System.out.println(parsed);
        System.out.println(">> Original timestamp = " + someDateSummerDay.getTime());
        System.out.println(">> Parsed timestamp = " + parsed.getTime());
        Assertions.assertFalse(parsed.getTime() == someDateSummerDay.getTime());


        /*
         * The big problem here is that to get the timestamp
         * I needed to know the corresponding timezone and timezone.
         *
         * If you need to pass timestamp values between systems,
         * prefer to pass the numeric value (the giant number) instead of
         * only one date. Or give us the full information: date,
         * time and offset (passing timezone alone may not be enough,
         * because in cases of overlap you will not know how to offset right)
         */
        final SimpleDateFormat simpleDateFormatPT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSXXX", LOCALE_PORTUGAL);
        simpleDateFormatPT.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        final String solving = simpleDateFormatPT.format(someDateSummerDay);


        final SimpleDateFormat rightParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSXXX", LOCALE_PORTUGAL);
        rightParser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        Assertions.assertEquals(someDateSummerDay.getTime(), rightParser.parse(solving).getTime());


        final ZonedDateTime zonedDateTime2 = YearMonth.of(2020, Month.JANUARY)
                .atDay(1)
                .atTime(0, 5, 45)
                .atZone(ZoneId.of(EUROPE_LISBON));
        final Date someDateWinterDay = Date.from(zonedDateTime2.toInstant());

        Assertions.assertEquals(rightParser.parse("2020-01-01T00:05:45:000Z").getTime(), zonedDateTime2.toInstant().toEpochMilli());
    }

    /**
     * {@link SimpleDateFormat} default behavior is "allow all", allow any value even the invalid values.
     * This behavior is called "lenient".
     * <p>
     * To avoid this behavior and parse only valid dates, we must cancel lenient behavior using the
     * {@link SimpleDateFormat#setLenient(boolean)} method with false parameter.
     */
    @Test
    void allowInvalidValuesByDefault() throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy", LOCALE_PORTUGAL);
        parser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        // we know that the the max day of month '2019-02' is 28. The SimpleDateFormater will sum up the extra days
        final Date parse = parser.parse("31-02-2019");
        final LocalDate localDate = LocalDate.ofInstant(parse.toInstant(), ZoneId.of(EUROPE_LISBON));
        Assertions.assertEquals(LocalDate.of(2019, 3, 3), localDate);


        final SimpleDateFormat notLenientParser = new SimpleDateFormat("dd-MM-yyyy", LOCALE_PORTUGAL);
        notLenientParser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));
        notLenientParser.setLenient(false);

        Assertions.assertThrows(java.text.ParseException.class,
                () -> notLenientParser.parse("31-02-2019"),
                "Unparseable date: \"31-02-2019\"");
    }

    /**
     * {@link SimpleDateFormat} is so lenient that sometimes it doesn't even validate Format.
     * In the next example, I use the pattern yyyyMMdd (year, month and day, without separators)
     * and pass a String  different for {@link SimpleDateFormat#parse(String)} method.
     * <p>
     * <p>
     * Since the String is in a different format from the pattern, the  expected would be an exception.
     * But {@link SimpleDateFormat} is so that parsing was successful.
     * But the result is completely wrong. Input string is February 1, 2018,
     * but the parse () method returned a Date that corresponds to 2 of  December 2017! What happened?
     * <p>
     * the SimpleDateFormat tries everything to do  parsing and only throws exception if there is no way at all.
     * In this case, when matching the yyyyMMdd pattern with the input 2018-02-01, we end up with the following values:
     *
     * <pre>
     * yyyyMMdd			<--	pattern
     * 2018-02-01		<--	input value
     * ano = 2018
     * mês = -0
     * dia = 2	(the "-" after the "2" is ignored)
     * the remaining values O ("01") are also ignored
     * </pre>
     * <p>
     * Having these values, parsing makes the appropriate adjustments.
     * The value of the month is -0, which is the same as 0.
     * <p>
     * And what does "month zero" mean?
     * If January is month 1 and year is 2018, then zero would be the previous month.
     * <p>
     * (December 2017). Day 2 is "right" (I mean it's a day valid for December 2017) and does not need any adjustments.
     * <p>
     * Then the Final result is December 2, 2017.
     * <p>
     * This can also be avoided by using {@link SimpleDateFormat#setLenient(boolean)} setLenient(false), which throws a ParseException if the
     * String not in same format as pattern.
     */
    @Test
    void tooMuchLenient() throws ParseException {
        final SimpleDateFormat parser = new SimpleDateFormat("yyyyMMdd", LOCALE_PORTUGAL);
        parser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        final Date date = parser.parse("2018-02-01");


        // the result is very different from what we expected
        Assertions.assertFalse(date.getTime() == YearMonth.of(2018, 2)
                .atDay(1)
                .atStartOfDay()
                .atZone(ZoneId.of(EUROPE_LISBON))
                .toInstant()
                .toEpochMilli());

        // the simple date format perform lots of adjustments a give
        Assertions.assertEquals(date.getTime(), YearMonth.of(2017, 12)
                .atDay(2)
                .atStartOfDay()
                .atZone(ZoneId.of(EUROPE_LISBON))
                .toInstant()
                .toEpochMilli());

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", LOCALE_PORTUGAL);
        parser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));
        parser.setLenient(false);

        Assertions.assertEquals("2017-12-02", formatter.format(date));
    }

    /**
     * {@link java.util.Date} and {@link java.util.Calendar}
     * works with millisecond precision of 3  decimal places in fractions of a second.
     * This means that they work well with values like 02:30:22.385 (2 hours, 30 minutes, 22 seconds and 385 milliseconds)
     * <p>
     * The fractions of second (385) can have a maximum of 3 digits. with values of 0 999.
     * This is the limit of this API, and any fraction with more than 3 decimal places is not supported,
     * so values like  02:30:22.3856 (4 decimal places) cannot be represented correctly.
     * <p>
     * In now days several APIs and databases already work with greater accuracy.
     * Some support microseconds (6 decimal places) and others are already in the nanoseconds (9 decimal places).
     * <p>
     * The problem arises when we try to use these values with greater precision than the API supports.
     * Because {@link SimpleDateFormat} works directly with Date, also has a limit of 3 decimal places.
     * The big problem is that, due to lenient behavior, this class accepts more than than 3 digits to parse.
     * Let's look at an example with String in ISO 8601 format and using microseconds (6 decimal places).
     */
    @Test
    void parsingOfFractionsBySecond() throws ParseException {
        //	pattern	with 6 digits of fractions of seconds
        final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", LOCALE_PORTUGAL);
        parser.setTimeZone(TimeZone.getTimeZone(EUROPE_LISBON));

        // The date is correct, but the time is wrong.
        // the input time is 10:20:30 but the time output is 10:22:33
        // why this happens?
        System.out.println(parser.parse("2018-02-01T10:20:30.123456"));


        // During the matching process, String to pattern, the values of year, month, day and others fields are obtained bu parsing.
        // Everything works fine until reach the milliseconds.

        /*
         * The value 123456 corresponds to the pattern SSSSSS. The letter S uppercase represents the milliseconds,
         * and as I used six letters, parsing checks for 6 digits that match them.
         * That is why, The value obtained is 123456. But 123456 milliseconds is "2 minutes, 3 seconds and 456 milliseconds ".
         * And this value is added to the time that had been obtained previously (10:20:30).
         * Therefore, the result is 10: 22: 33,456
         *
         * That is, when using more than 3 decimal places in fractions of second (more than 3 letters S in the pattern)
         * you get values wrong in both parsing and formatting.
         *
         * But the big problem, is the fact that the parsing throw no exception and return a Date as if nothing had happened.
         * Only this Date has a value incorrect, and you will only notice when it is too late.
         * Like this As in the previous examples, this can be avoided by using setLenient (false), which throws a ParseException
         * if the entry has more than 3 decimal places
         */

        /*
         * So, the question is: How to parse String with more than 3 digits of milliseconds?
         *
         * The answer is:
         * We can not do it. At least, not with SimpleDateFormat.
         *
         * You have probably seen an article on the internet saying to simply use multiple letters S.
         * And maybe you did it, after all "no mistake, so it worked!", right?
         * Using more than 3 letters S in the pattern only works if all the digits are zero.
         * If the entry is 2018-02-01T10:20:30.000000, the last 6 digits will be interpreted zero milliseconds,
         * so it will not affect the end result. But it is the only case where it "works".
         *
         * Any other value will generate a wrong date.
         *
         * Even if the value has multiple leading zeros, such as .100000.
         * This will be interpreted as 100000 (one hundred thousand) milliseconds (which 1 minute and 40 seconds)
         * and this will be added to the result.
         *
         * If the input String has more than 3 decimal places, The solution is to leave only the first 3 digits,
         * removing all the others.
         *
         * If you do not want to lose accuracy, you can save the excess digits in a separate field.
         * The other alternative, of course, is to use the java.time API, which has nanosecond accuracy (9 decimal places)
         */
    }

    /**
     * The {@link SimpleDateFormat} is not thread-safe.
     * <p>
     * To prove the problem we can analise the next example.
     * It was expected that all parsed and formatted String produces always the same result as the input. However we will have cases are that do not happens.
     * <p>
     * <p>
     * This problem  is because the implementation of SimpleDateFormat saves intermediate parsing results in variables of instance.
     * Internally, it has a Calendar field, which is used to store these results as fields are interpreted.
     * And this same Calendar is also used to format.
     * <p>
     * In short, what can happen is: while a thread is in the middle of parsing, with Calendar containing several
     * intermediate values obtained from the input String, other thread may be formatting, and end up printing these values.
     * Depending on the status of Calendar, these operations may throw an exception.
     */
    @Test
    void multithreadingProblem() throws InterruptedException {
        final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

        final String input = "01-02-2018";

        final ExecutorService pool = Executors.newCachedThreadPool();
        // create 100 threads
        IntStream.rangeClosed(1, 100)
                .mapToObj(index -> (Runnable) () -> {
                    try {
                        final Date parsed = SDF.parse(input);
                        String parsedFormatted = SDF.format(parsed);

                        // if the String are different we print the both
                        if (!input.equals(parsedFormatted)) {
                            System.out.printf("%d >> Input:  [%s] is different of the output [%s]\n", index, input, parsedFormatted);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(pool::submit);
        pool.awaitTermination(15, TimeUnit.SECONDS);
    }

    /**
     * To avoid the previous problem, we just simply do not share the SimpleDateFormat across multiple threads,
     * creating a new instance every loop iteration.
     * Therefore, 100 will be created instances, which will not be shared between threads, solving problem.
     * <p>
     * <p>
     * {@link Date} and {@link java.util.Calendar} also suffer from the same problem: they are not thread-safe and may have problems if an instance is
     * shared across multiple threads. And the techniques to avoid these Problems are the same as those used with SimpleDateFormat:  synchronization and cloning.
     */
    @Test
    void multithreadingSolution() throws InterruptedException {
        final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

        final ThreadLocal<SimpleDateFormat> TL = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("dd-MM-yyyy");
            }
        };

        final AtomicLong problems = new AtomicLong(0);

        final String input = "01-02-2018";

        final ExecutorService pool = Executors.newCachedThreadPool();
        // create 100 threads
        IntStream.rangeClosed(1, 100)
                .mapToObj(index -> (Runnable) () -> {
                    try {

                        //final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
                        //final SimpleDateFormat sdf = (SimpleDateFormat) SDF.clone();
                        final SimpleDateFormat sdf = TL.get();

                        final Date parsed = sdf.parse(input);
                        String parsedFormatted = sdf.format(parsed);

                        // if the String are different we print the both
                        if (!input.equals(parsedFormatted)) {
                            problems.incrementAndGet();
                            System.out.printf("%d >> Input:  [%s] is different of the output [%s]\n", index, input, parsedFormatted);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(pool::submit);
        pool.awaitTermination(15, TimeUnit.SECONDS);

        Assertions.assertEquals(0L, problems.get());
    }
}
