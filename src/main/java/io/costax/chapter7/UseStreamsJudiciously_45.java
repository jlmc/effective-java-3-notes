package io.costax.chapter7;

import java.math.BigInteger;
import java.util.stream.Stream;

public class UseStreamsJudiciously_45 {

    /*
     * Mersenne number is a number of the form (2^p) -1
     *
     * if p is prime, the corresponding mersenne number my be prime
     * if it is, it's a Mersenne prime
     */


    private static Stream<BigInteger> primes() {
        return Stream.iterate(BigInteger.ONE, BigInteger::nextProbablePrime);
    }

    public static void main(String[] args) {
        //@formatter:off
        primes()
                .map(p -> BigInteger.TWO.pow(p.intValueExact()).subtract(BigInteger.ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(50)
                .forEach(System.out::println);
        //@formatter:on
    }
}
