package io.costax.puzzlers;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Tom goes to the auto parts store to buy a spark plug that costs $1.10, but all he
 * has in his wallet are two-dollar bills. How much change should he get if he pays
 * for the spark plug with a two-dollar bill?
 */
public class Charge {

    @Test
    public void test() {

        //System.out.println(2.00 - 1.10); // you might expect the program to print 0.90, but we get  0.8999999999999999
        final double x = 2.00 - 1.10;
        System.out.println(x); // you might expect the program to print 0.90, but we get  0.8999999999999999

        /*
         * The problem is that the number 1.1 can’t be represented exactly as a double,
         * so it is represented by the closest double value. The program subtracts this value
         * from 2. Unfortunately, the result of this calculation is not the closest double value
         * to 0.9. The shortest representation of the resulting double value is the hideous
         * number that you see printed
         */

        // Poor solution - still uses binary floating-point!
        System.out.printf("%.2f%n", x);


        /*
         * Another way to solve the problem is to use BigDecimal, which performs
         * exact decimal arithmetic. It also interoperates with the SQL DECIMAL type via
         * JDBC.
         *
         * There is one caveat: Always use the BigDecimal(String) constructor,
         * never BigDecimal(double).
         *
         * The latter constructor creates an instance with the
         * exact value of its argument: new BigDecimal(.1) returns a BigDecimal
         * representing 0.1000000000000000055511151231257827021181583404541015625.
         * Using BigDecimal correctly, the program prints the expected result of 0.90:
         */
        System.out.println(new BigDecimal("2.00").subtract(new BigDecimal("1.10")));

    }
}
