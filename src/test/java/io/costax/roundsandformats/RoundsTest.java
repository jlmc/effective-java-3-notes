package io.costax.roundsandformats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * <table>
 *     <thead>
 *         <th>employee</th>
 *         <th>Value</th>
 *     </thead>
 *     <tbody>
 *         <tr><td>marcelo</td><td>7.3213832</td></tr>
 *         <tr><td>Diogo</td><td>6.18317113</td></tr>
 *         <tr><td>Gonsalo</td><td>9.3181</td></tr>
 *     </tbody>
 * </table>
 */
class RoundsTest {

    private List<EmployeeSalaryDouble> getEmployeeSalaryDoubles() {
        return List.of(
                EmployeeSalaryDouble.of("Marcelo", 7.3213832D),
                EmployeeSalaryDouble.of("Diogo", 6.18317113D),
                EmployeeSalaryDouble.of("Gonsalo", 9.3181D));
    }

    private double calculateAvg(final List<EmployeeSalaryDouble> employeeSalaryDoubles) {
        final Double total = employeeSalaryDoubles.stream()
                .map(EmployeeSalaryDouble::getValue)
                .reduce(0.0, Double::sum);

        return total / employeeSalaryDoubles.size();
    }

    @Test
    void avgDoublesWithoutRounds() {
        final List<EmployeeSalaryDouble> employeeSalaryDoubles = getEmployeeSalaryDoubles();

        double avg = calculateAvg(employeeSalaryDoubles);

        System.out.println(avg); // >>> 7.607551443333333
    }

    /**
     * For rounding in doubles we can use the {@link Math#round(double)} static method.
     * <p>
     * But the actual value would be 7.607551443333333 and we got 8. So it's not very accurate, right?
     * This is because according the documentation the round method returns the longest closest to the actual value passed in the parameter.
     * In our case, we have approximately the real value of 7.6. So the longest nearest is 8.
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html">Math#round</a>
     */
    @Test
    void roundingWithMath() {
        final double avg = calculateAvg(getEmployeeSalaryDoubles());

        System.out.println(avg);
        System.out.println(Math.round(avg));
    }

    /**
     * To improve this rounding, we can use a workaround. Thinking that the round () method rounds to the nearest long,
     * we can multiply the average by 100, use round () and then divide by 100. Doing this in code, we have:
     */
    @Test
    void rounding() {
        final double avg = calculateAvg(getEmployeeSalaryDoubles());

        //System.out.println(avg);
        //System.out.println(Math.round(avg));

        // note that with this method we may not always have 2 decimal digits of precision, we some case we may have just 1 or even none..
        final double roundAvg = Math.round(avg * 100D) / 100D;
        //System.out.println(roundAvg);

        Assertions.assertEquals(7.607551443333333, avg, 0.000000001);
        Assertions.assertEquals(8, Math.round(avg));
        Assertions.assertEquals(7.61D, roundAvg);

        // to String DecimalFormat df = new DecimalFormat("0.00");
        final DecimalFormat decimalFormat = new DecimalFormat("0.00");
        //decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        Assertions.assertEquals("7.61", decimalFormat.format(avg));
    }

    private static class EmployeeSalaryDouble {
        private String name;
        private double value;

        private EmployeeSalaryDouble(final String name, final double value) {
            this.name = name;
            this.value = value;
        }

        public static EmployeeSalaryDouble of(final String name, final double value) {
            return new EmployeeSalaryDouble(name, value);
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }
    }
}
