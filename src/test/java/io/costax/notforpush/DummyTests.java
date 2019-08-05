package io.costax.notforpush;


import io.costax.BigDecimals;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class DummyTests {

    @Test
    public void noRound() {
        BigDecimal bigDecimal = new BigDecimal("5.999");


        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);

        bigDecimal = new BigDecimal("5.110");
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);

        bigDecimal = new BigDecimal("5.111");
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);

        bigDecimal = new BigDecimal("5.114");
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);

        bigDecimal = new BigDecimal("5.115");
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);

        bigDecimal = new BigDecimal("5.116");
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN) + "-- " + bigDecimal);
    }

    @Test
    public void roundUpBigDecimal() {

        final List<BigDecimal> bigDecimals = List.of(
                new BigDecimal("5.019"),
                new BigDecimal("5.014"),
                new BigDecimal("5.015"),
                new BigDecimal("5.0112"),
                new BigDecimal("5.0112"),
                new BigDecimal("5.000"),
                new BigDecimal("5.091"),
                new BigDecimal("5.999")
        );




        shows(bigDecimals,  RoundingMode.HALF_UP);
        shows(bigDecimals,  RoundingMode.HALF_EVEN);
        shows(bigDecimals,  RoundingMode.HALF_DOWN);
        shows(bigDecimals,  RoundingMode.CEILING);
        //shows(bigDecimals,  RoundingMode.UNNECESSARY);


    }

    public void shows(final List<BigDecimal> bigDecimals, final RoundingMode roundingMode) {

        System.out.println("-------- " + roundingMode + " -------------");
        bigDecimals.forEach(v -> {
            final BigDecimal y = v.setScale(2, roundingMode);
            final BigDecimal x = v.round(new MathContext(2 + 1, roundingMode));
            System.out.println(y + " -- " + x);

        });
    }

    @Test
    public void roundWorkarrown2() {
        System.out.printf("%f \n", round(14.01, 2));
        System.out.printf("%f \n", round(14.010, 2));
        System.out.printf("%f \n", round(14.011, 2));
        System.out.printf("%f \n", round(14.012, 2));
        System.out.printf("%f \n", round(14.015, 2));
        System.out.printf("%f \n", round(14.016, 2));
        System.out.printf("%f \n", round(14.019, 2));
    }

    @Test
    public void roundWorkarrown0() {
        System.out.printf("%f \n", round(14.01, 0));
        System.out.printf("%f \n", round(14.010, 0));
        System.out.printf("%f \n", round(14.011, 0));
        System.out.printf("%f \n", round(14.012, 0));
        System.out.printf("%f \n", round(14.015, 0));
        System.out.printf("%f \n", round(14.016, 0));
        System.out.printf("%f \n", round(14.919, 0));
    }

    static float round (double value, int scale) {
        //int scale = 0 or 2
        float p = (float) Math.pow(10, scale);
        value = value * p;
        float tmp = Math.round(value);
        return (float) tmp / p;
    }

    public static Integer toIntegerOrNull(final String value) {
        return Optional.ofNullable(value)
                .map(String::valueOf)
                .map(String::trim)
                .map(x -> new Scanner(x).findInLine("^\\d+$"))
                .map(Integer::valueOf).orElse(null);
    }

    @Test
    public void finnalyTest() {
        try {

            System.out.println("---->>> execution");
            throw new IllegalStateException("---");

        } catch (Exception e) {
            System.out.println("--- Catch" + e);

        } finally {
            System.out.println("--- Finnaly");
        }

    }

    @Test
    public void toIn() {
        System.out.println(toIntegerOrNull("abc"));
        System.out.println(toIntegerOrNull("1234"));
        System.out.println(toIntegerOrNull(""));
        System.out.println(toIntegerOrNull("---123abd"));
        System.out.println(toIntegerOrNull(null));

    }

    @Test
    public void as() {
        Calendar cal = Calendar.getInstance();


        cal.setTime(new Date());
        System.out.println("" + cal.getTime());


        cal.add(Calendar.DATE, 30);

        System.out.println("" + cal.getTime());

        System.out.println(LocalDate.now().plusDays(30));
    }


    @Test
    public void formBigDecimal() {

        BigDecimal v = new BigDecimal("981.896");

        System.out.println(v);
        System.out.println(BigDecimals.formatWithoutRounding(v));

    }

    @Test
    public void regexDecimal() {

        //final Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");

        //final String regex = "^-?[0-9]{1,3}(\\.[0-9]*)?$";
        //final String regex = "^-?[0-9]{1,13}(\\.\\d+)?$";
        final String regex = "^[-+]?[0-9]*\\.?[0-9]+$";

        //System.out.println("1.2".matches("[0-9]*\\.?[0-9]*"));
        //System.out.println("1,2".matches("[0-9]*\\.?[0-9]*"));
        //System.out.println("1,2".matches("^[0-9]*\\.?[0-9]*$"));
        //System.out.println("921".matches("^[0-9]*\\.?[0-9]*$"));
        //System.out.println(".00".matches(regex));


        final BigDecimal bigDecimal1 = new BigDecimal("12.12");
        final BigDecimal bigDecimal2 = new BigDecimal(".09");
        final BigDecimal bigDecimal3 = new BigDecimal("+12.09");
        final BigDecimal bigDecimal4 = new BigDecimal("-12.09");


        Assert.assertTrue(".00".matches(regex));
        Assert.assertTrue("1".matches(regex));
        Assert.assertTrue("12".matches(regex));
        Assert.assertTrue("12".matches(regex));
        Assert.assertTrue("123".matches(regex));
        // Assert.assertFalse("1234".matches(regex));

        Assert.assertTrue("123.123".matches(regex));
        Assert.assertTrue("   123.123".trim().matches(regex));
        Assert.assertFalse("123,123".matches(regex));
        Assert.assertFalse("   123.123".matches(regex));
        Assert.assertFalse("123.123  ".matches(regex));
        Assert.assertTrue("-123.123".matches(regex));
        Assert.assertFalse("-123.".matches(regex));


        final DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        System.out.println(decimalFormat.format(new BigDecimal("12345.91")));
        System.out.println(decimalFormat.format(new BigDecimal(".91")));
    }

    @Test
    public void calcDaysBetween() {
        System.out.println(Objects.equals(null, null));

        final LocalDateTime ten = LocalDateTime.of(2019, 7, 10, 10, 0);
        final LocalDateTime one = LocalDateTime.of(2019, 7, 1, 10, 0);

        final long between = ChronoUnit.DAYS.between(one, ten);


        System.out.println(between);

        final long l = Duration.between(ten, one).toDays();// another option
        System.out.println(l);
    }

    /*
    @Test
    public void reduceStreamOptionToMap() {


        Dependencia x = new Dependencia();
        Organizacao y = new Organizacao("abc");

        List<ReportDocumentFormField> reportFormFieldsMetadata = TipoDocumento.ListaNovasColheitas.getReportFormFieldsMetadata();

        Map<Object, Object> collect = Stream.of(
                reportFormFieldsMetadata.stream().filter(p -> p.isTypeOf(Organizacao.class)).map(r -> new AbstractMap.SimpleEntry(r, y)).findFirst(),
                reportFormFieldsMetadata.stream().filter(p -> p.isTypeOf(Dependencia.class)).map(r -> new AbstractMap.SimpleEntry(r, x)).findFirst(),
                reportFormFieldsMetadata.stream().filter(p -> p.isTypeOf(LocalDate.class) && ReportKeys.DATA_INICIO.equalsIgnoreCase(p.getReportKey())).map(r -> new AbstractMap.SimpleEntry(r, LocalDate.now())).findFirst(),
                reportFormFieldsMetadata.stream().filter(p -> p.isTypeOf(LocalDate.class) && ReportKeys.DATA_FIM.equalsIgnoreCase(p.getReportKey())).map(r -> new AbstractMap.SimpleEntry(r, LocalDate.now())).findFirst()
        )
        .flatMap(Optionals::toStream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(collect);


       // <ReportDocumentFormField, Object>
    }
    */

    @Test
    public void showMap() {

        Map<String, java.io.Serializable> map = new HashMap<>();

        map.put("a", Integer.valueOf(1));
        map.put("b", "List<");

        System.out.println(map);

    }

    @Test
    public void blankStringPattern() {
        Pattern p = Pattern.compile("^(?!\\s*$).+$");

        System.out.println(p.matcher("").matches());
        System.out.println(p.matcher(" ").matches());
        System.out.println(p.matcher("  ").matches());
        System.out.println(p.matcher("  1").matches());
        System.out.println(p.matcher("1   ").matches());
        System.out.println(p.matcher("  1 ").matches());

    }

    @Test
    public void IntegerRegex() {
        Pattern p = Pattern.compile("\\d+");

        System.out.println(p.matcher("123").matches());
        System.out.println(p.matcher("").matches());
       // System.out.println(p.matcher(null).matches()); // not
        System.out.println(p.matcher("foo123").matches());
        System.out.println(p.matcher("123foo").matches());
        System.out.println(p.matcher("12acf3").matches());
        System.out.println(p.matcher("12.3").matches());
    }


    @Test
    public void casting() {
        Integer cast = null;
        System.out.println(cast);
    }

    @Test
    public void name123() throws Exception {

        /*
        Class<TurnoSlot> slotClass = TurnoSlot.class;
        Constructor<TurnoSlot> constructor = slotClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        TurnoSlot slot1 = constructor.newInstance();

        //ReflectionUtils.findField(TurnoSlot.class, "id");
        String id = "id";
        Class<?> searchType = slotClass;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();

            for (Field field : fields) {
                if (id.equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(slot1, 50);
                    break;
                }
            }
            searchType = searchType.getSuperclass();
        }

        //Field id = slotClass.getSuperclass().getDeclaredField("id");
        //id.setAccessible(true);
        //id.set(slot1, 50);

        System.out.println(slot1.getId());
    */
    }

    @Test
    public void dosEquis() {
        char x = 'X';
        int i = 0;
        System.out.print(true ? x : 0);
        System.out.print(false ? i : x);

        // expected: XX
    }

    @Test
    public void stringValueOf() {

        Object str = null;
        String[] values = null;
        char[] as = null;

        System.out.println(1);
        System.out.println("abcde");
        System.out.println(str);
        System.out.println(values);
        //System.out.println(String.valueOf(as));
    }


    @Test
    public void alturaRegex() {

        Pattern pattern = Pattern.compile("^[0-2]{1}.[0-9]{1,2}$");

        matcher(pattern, "1.12");
        matcher(pattern, "0.1");
        matcher(pattern, "2.9");
        matcher(pattern, "2.9");
        matcher(pattern, "12.9");
        matcher(pattern, "12.9");


    }

    private void matcher(Pattern pattern, String value) {
        System.out.println(value + " -- " + pattern.matcher(value).matches());
    }

    @Test
    public void currentDirectory() throws IOException {

        File directory = new File(".");

        System.getProperty(directory.getCanonicalPath() + File.separator + "my.properties");

        System.getProperty("." + File.separator + "my.properties");

        System.out.println(directory);
        System.out.println(directory.getCanonicalPath());

    }

    @Test
    public void extractDocNumber() {
        String number = "A18/259605";

        final String[] split = number.split(".*/");

        System.out.println(Arrays.toString(split));

        System.out.println(number.replaceAll(".*/(.*?)", "$1"));
        System.out.println(number.replaceAll(".*/(./d*?)", "$1"));
    }

    @Test
    public void optionalOrElse() {
        String variable = "";
        final String s = Optional.ofNullable(variable).orElseGet(() -> somethingVeryDiferent());
        System.out.println(">>>>" + s);

    }

    private String somethingVeryDiferent() {
        System.out.println("--- getting somethingVeryDiferent");
        return "something-Very-Diferent";
    }

    @Test
    public void name1234567() {

        List<Fly> flies = new ArrayList<>();

        flies.add(new Fly("galo locas"));
        flies.add(new Papagaio(34));
        handler(flies);
    }

    public void handler(List<? extends Fly> flies) {

        for (Fly fly : flies) {
            final String name = fly.getName();
            System.out.println(name);
        }
        // error: compilation error
        //flies.add((Fly)new Papagaio(123));

    }

    @Test
    public void testSet() {
        Set<Foo> foos = new TreeSet<>();

        foos.add(new Foo(1));
        foos.add(new Foo(2));
        foos.add(new Foo(3));

        System.out.println(foos.size());

        foos.forEach(System.out::println);

        foos.remove(new Foo(1));

        System.out.println("***");

        foos.forEach(System.out::println);
    }

    @Test
    public void stringSize() {

        String s = "2018-11-08;2018-11-08;FT A18/86393;0.00;2018-11-07;2018-11-07;FT A18/86392;0.00;2018-11-07;2018-11-07;FT A18/86391;0.00;suSN6Nvh2TqyP7HnH8ip+XdbFy2+pq/78sLTr1reqGjHBGv37yflYmvWqhOypVtV5MHCXT1SJt8lKDCdtawVmM6ggS5MBJp+WF8NoL/Vsg2XwzgMb0d7ppGlk7nq7zFjq+2kZlItkhkcexPZiOrNgMROTJGKq20KXwr2pl+iIBE=";

        final char[] chars = s.toCharArray();

        System.out.println(">>>" + chars.length);
    }

    @Test
    public void spkipSaturdaysAndSundays() {

        // long minutes = 7200L;
        long minutes = 75674L;
        long numberOfDays = TimeUnit.MINUTES.toDays(minutes);
        //long numberOfDays = TimeUnit.MINUTES.toHours(7200L);
        Duration duration = Duration.of(minutes, ChronoUnit.MINUTES);


        System.out.println("days>> " + numberOfDays);
        System.out.println("days>> " + duration);

        LocalDateTime fromDate = LocalDateTime.now();

        LocalDateTime localDateTime = fromDate.plusMinutes(minutes);

        System.out.println("from: " + fromDate);
        System.out.println("to: " + localDateTime);

        while (DayOfWeek.SATURDAY == localDateTime.getDayOfWeek() || DayOfWeek.SUNDAY == localDateTime.getDayOfWeek()) {
            localDateTime = localDateTime.plusDays(1L);
        }

        System.out.println(">>" + localDateTime + " >> " + localDateTime.getDayOfWeek());

        int hour = localDateTime.getHour();

        if (hour < 8) {
            localDateTime = localDateTime.withHour(8).withMinute(0).withSecond(0).withNano(0);
        } else if (hour >= 19) {
            //
            localDateTime = localDateTime.plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        }


        System.out.println(">>>" + localDateTime);


    }

    @Test
    public void hoursToMinutes() {


        long l1 = TimeUnit.MINUTES.toDays(7200L);

        System.out.println("days>> " + l1);


        long l = TimeUnit.HOURS.toMinutes(10);
        System.out.println(l);

    }

    @Test
    public void periods() {
        // TODO:
        Duration duration = Duration.between(LocalTime.now(), LocalTime.now().plusHours(1));

        System.out.println(duration);
        System.out.println(duration.toHours());


        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    public void bigDecimalVsDecimal() {
        BigDecimal decimal = BigDecimal.valueOf(123456, 2);
        System.out.println(decimal);

        double a = 0.02D;
        double b = 0.03D;
        double c = b - a;
        System.out.println(c); // expected: 0.01

        BigDecimal _a = new BigDecimal("0.02").setScale(1, RoundingMode.HALF_UP);
        System.out.println(_a);
        BigDecimal _b = new BigDecimal("0.03");
        BigDecimal _c = _b.subtract(_a);
        System.out.println(_c); // expected: 0.01
    }

    @Test
    public void diferenceBetweenBigDecimal() {
        double acceptedDiferrence = 0.01D;

        BigDecimal a = BigDecimal.valueOf(5.00);
        BigDecimal b = BigDecimal.valueOf(5.02);

        BigDecimal subtract = a.subtract(b);
        System.out.println(subtract);

        System.out.println(subtract.abs());

        System.out.println(BigDecimals.isDifferenceIsGreaterThan(a, b, acceptedDiferrence));


    }

    @Test
    public void localDateTimeFormater() {


        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

    }

    @Test
    public void bigDecimalScala() {
        BigDecimal bigDecimal = BigDecimal.valueOf(123, 9876);

        System.out.println(bigDecimal.scale());
        System.out.println(bigDecimal.unscaledValue());

        System.out.println(bigDecimal.compareTo(BigDecimal.valueOf(123.98)));
        System.out.println(bigDecimal.compareTo(BigDecimal.valueOf(123)));
        System.out.println(bigDecimal.compareTo(BigDecimal.valueOf(123.9876)));
        System.out.println(bigDecimal);


    }

    @Test
    public void setofIntergersFromArray() {

        String format = String.format("%s 2%%", "ABC 123");
        System.out.println(format);

        Set<Integer> integers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(84,
                91,
                1075,
                1098,
                100883,
                401082,
                401083,
                401084,
                401085,
                401086,
                401164,
                401165,
                401243,
                401163)));

        integers.stream().forEach(i -> System.out.println(i.getClass() + " --- " + i));
    }

    @Test
    public void formatWith() {

        final YearMonth yearMonth = YearMonth.now();
        String format = String.format("%s %d/%d", "hello-mother", yearMonth.getYear(), yearMonth.getMonthValue());
        System.out.println(format);

    }

    @Test
    public void calendarMonths() {

        LocalDateTime localDateTime = LocalDateTime.of(2017, 8, 10, 23, 10, 15);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        System.out.println(calendar.get(Calendar.MONTH));
    }

    @Test
    public void testFormtString() {

        String abcd = String.format("%s%%", "abcd");
        System.out.println(abcd);
    }

    @Test
    public void compareLocalDateTime() {

        /*
         private LocalDateTime calculateFromDate() {
        return this.items.stream()
                .map(CaixaItem::getDate)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private BigDecimal calculateTotalSecurityDeposit() {
        return this.items.stream()
                .filter(CaixaItem::isSecurityDeposit)
                .map(CaixaItem::getSecurityDeposit)
                .filter(c -> !EstadoCaucao.NULLIFIED.equals(c.getStatus()))
                .map(Caucao::getValue)
                .reduce(ZERO, BigDecimal::add);
    }
         */


        final LocalDateTime recibodate = LocalDateTime.of(
                LocalDate.of(2018, Month.FEBRUARY, 1),
                LocalTime.of(13, 34, 1));


        final LocalDateTime recibo2date = LocalDateTime.of(
                LocalDate.of(2018, Month.JANUARY, 31),
                LocalTime.of(9, 15, 1));


        final LocalDateTime noRecibodate = LocalDateTime.of(
                LocalDate.of(2018, Month.JANUARY, 1),
                LocalTime.of(13, 34, 1));

        List<LocalDateTime> items = Arrays.asList(recibo2date, recibodate, noRecibodate);

        LocalDateTime min = items.stream()
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime max = items.stream()
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        System.out.println(min);
        System.out.println(max);
    }

    @Test
    public void periodo() {

        LocalDate of = LocalDate.of(2012, Month.OCTOBER, 1);
        LocalDate now = LocalDate.now();
        Period between = Period.between(of, now);

        System.out.println(between);

        Duration between1 = Duration.between(
                LocalDateTime.of(of, LocalTime.MIN),
                LocalDateTime.of(now, LocalTime.MIN));
        System.out.println(between1);


    }

    @Test
    public void name() {

        BigDecimal one = BigDecimal.valueOf(1.0D);

        BigDecimal multiply = one.multiply(BigDecimal.TEN);

        System.out.println(multiply == one);
        System.out.println(multiply.compareTo(one));
        System.out.println(multiply.equals(one));
        System.out.println(one);
        System.out.println(multiply);
    }

    @Test
    public void addOnBigDecimal() {

        BigDecimal accumulator = BigDecimal.ZERO;

        BigDecimal add = accumulator.add(BigDecimal.valueOf(7.0D));

        System.out.println(accumulator);
        System.out.println(add);

    }

    @Test
    public void formatDateTest() {

        String format = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now());
        System.out.println(format);
    }

    static class Ifs {
        public void method() {
            System.out.println("-----");
        }
    }

    public static class Foo implements Comparable<Foo> {
        int id;

        public Foo(final int id) {
            this.id = id;
        }


        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Foo foo = (Foo) o;
            return id == foo.id;
        }
/*
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
*/

        @Override
        public String toString() {
            return "Foo{" +
                    "id=" + id +
                    '}';
        }

        @Override
        public int compareTo(final Foo o) {
            return Integer.compare(o.id, this.id);
        }
    }

    static class Person {
        private int id;
    }

    static class SuperPerson {
        private String name;
    }

    public class Fly {
        private String name;

        public Fly(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }

    public class Papagaio extends Fly {

        private int a;

        public Papagaio(final int i) {
            super("Papagaio");
            a = i;
        }

        public int getA() {
            return a;
        }

        public void setA(final int a) {
            this.a = a;
        }
    }
}

class MyHandler implements InvocationHandler {

    private Object target;

    MyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            System.out.println("on my my Handler");
            return method.invoke(this.target, args);
        } catch (Exception e) {

            return null;// ???
        }
    }
}
