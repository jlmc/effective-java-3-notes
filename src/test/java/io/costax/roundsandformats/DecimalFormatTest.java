package io.costax.roundsandformats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

class DecimalFormatTest {

    static Map<Locale, DecimalFormat> DECIMAL_FORMATS = new HashMap<>();

    /**
     * Symbol	Description
     * 0	a digit
     * #	a digit, zero shows as absent
     * .	placeholder for decimal separator
     * ,	placeholder for grouping separator
     * E	separates mantissa and exponent for exponential formats
     * ;	separates formats
     * -	default negative prefix
     * %	multiply by 100 and show as percentage
     * ?	multiply by 1000 and show as per mille
     * ¤	currency sign; replaced by currency symbol; if doubled, replaced by international currency symbol; if present in a pattern, the monetary decimal separator is used instead of the decimal separator
     * X	any other characters can be used in the prefix or suffix
     * '	used to quote special characters in a prefix or suffix
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html">DecimalFormat java doc</a>
     */
    private static DecimalFormat getDecimalFormat(final Locale locale) {
        if (DECIMAL_FORMATS.containsKey(locale)) {
            return DECIMAL_FORMATS.get(locale);
        }

        // to String DecimalFormat df = new DecimalFormat("0.00");
        final DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
        //final DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
        //final DecimalFormat decimalFormat = new DecimalFormat("#,###.00", decimalFormatSymbols);
        final DecimalFormat decimalFormat = new DecimalFormat("#,###.00 ¤", decimalFormatSymbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        //decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        decimalFormat.setCurrency(Currency.getInstance(locale));

        DECIMAL_FORMATS.put(locale, decimalFormat);

        return decimalFormat;
    }

    /**
     * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html">DecimalFormat java doc</a>
     */
    @Test
    void formatting() {

        final double x = 123_456_789.607_551_443_333_333D;
        final double a = 7.607551443333333D;
        final double b = 7.60456789D;
        final double c = 7.60D;
        final double d = 7.65D;
        final double e = 7.66D;
        final double f = 7.6D;
        final double g = 7.0D;

        final Locale pt = new Locale("pt", "PT");
        final Locale br = new Locale("pt", "BR");
        final Locale de = new Locale("de", "DE");
        final Locale us = Locale.US;
        final List<Locale> locales = Arrays.asList(pt, br, de, us);

        locales.forEach(l -> System.out.println(l + " -> " + getDecimalFormat(l).format(x) + " " + Currency.getInstance(l).getCurrencyCode()));

        Assertions.assertEquals("123 456 789,61 €", getDecimalFormat(pt).format(x));
        Assertions.assertEquals("123.456.789,61 R$", getDecimalFormat(br).format(x));
        Assertions.assertEquals("123,456,789.61 $", getDecimalFormat(us).format(x));
        Assertions.assertEquals("123.456.789,61 €", getDecimalFormat(de).format(x));

        /*System.out.println(decimalFormat.format(x));
        System.out.println(decimalFormat.format(a));
        System.out.println(decimalFormat.format(b));
        System.out.println(decimalFormat.format(c));
        System.out.println(decimalFormat.format(d));
        System.out.println(decimalFormat.format(e));
        System.out.println(decimalFormat.format(f));
        System.out.println(decimalFormat.format(g));
        */
    }

    @Test
    void showCurrency() {
        final Currency currencyPT = Currency.getInstance(new Locale("pt", "PT"));
        System.out.println(currencyPT);
    }

    @Test
    void getDefaultFormatterForAGivenLocale() {
        final NumberFormat pt = NumberFormat.getInstance(new Locale("pt", "PT"));

        final double x = 123_456_789.607_551_443_333_333D;

        Assertions.assertEquals("123 456 789,608", pt.format(x));
        final Class<? extends NumberFormat> aClass = pt.getClass();
        Assertions.assertTrue(pt.getClass().isAssignableFrom(DecimalFormat.class));
    }
}
