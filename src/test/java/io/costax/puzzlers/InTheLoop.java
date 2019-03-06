package io.costax.puzzlers;

import io.costax.TimerMarker;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Puzzle 26
 */
public class InTheLoop {

    private static final int END = Integer.MAX_VALUE;  // 2147483647
    private static final int START = END - 100;
    @Rule
    public TimerMarker timer = TimerMarker.timer();

    @Test(timeout = 60000L)
    @Ignore
    public void inTheLoop() {
        //System.out.println("" + START);
        //System.out.println("" + END);

        int count = 0;

        for (int i = START; i <= END; i++) {
            count++;
        }

        System.out.println(count);
    }

    /**
     * <pre>
     *
     * If you don’t look at the program very carefully, you might think that it prints 100;
     * after all, END is 100 more than START. If you look a bit more carefully, you will see
     * that the program doesn’t use the typical loop idiom. Most loops continue as long
     * as the loop index is less than the end value, but this one continues as long as the
     * index is less than or equal to the end value. So it prints 101, right? Well, no. If you
     * ran the program, you found that it prints nothing at all. Worse, it keeps running
     * until you kill it. It never gets a chance to print count, because it’s stuck in an infinite loop.
     *
     * The problem is that the loop continues as long as the loop index (i) is less
     * than or equal to Integer.MAX_VALUE, but all int variables are always less than or
     * equal to Integer.MAX_VALUE. It is, after all, defined to be the highest int value in
     * existence. When i gets to Integer.MAX_VALUE and is incremented, it silently
     * wraps around to Integer.MIN_VALUE.
     *
     * If you need a loop that iterates near the boundaries of the int values, you are
     * better off using a long variable as the loop index. Simply changing the type of the
     * loop index from int to long solves the problem, causing the program to print 101
     * as expected:
     *
     * <code>
     * for (long i = START; i <= END; i++)
     * </code>
     *
     *
     * More generally, the lesson here is that ints are not integers.
     * Whenever you  use an integral type, be aware of the boundary conditions.
     * What happens if the value underflows or overflows?
     * Often it is best to use a larger type. (The integral types are byte, char, short, int, and long.)
     *
     * It is possible to solve this problem without resorting to a long index variable, but it’s not pretty:
     *
     * <code>
     * int i = START;
     * do {
     *     count++;
     * } while (i++ != END);
     * </code>
     *
     *
     * Given the paramount importance of clarity and simplicity, it is almost always
     * better to use a long index under these circumstances, with perhaps one exception:
     *
     * If you are iterating over all (or nearly all) the int values, it’s about twice as fast to stick with an int.
     * Here is an idiom to apply a function f to all four billion int values:
     *
     * <code>
     *
     * // Apply the function f to all four billion int values
     * int i = Integer.MIN_VALUE;
     * do {
     *      f(i);
     * } while (i++ != Integer.MAX_VALUE)
     * </code>
     *
     * </pre>
     */

    @Test
    public void solution1() {
        int count = 0;

        for (long i = START; i <= END; i++) {
            count++;
        }

        System.out.println(count);
    }

    @Test
    public void solution2() {
        int count = 0;

        int i = START;
        do {
            count++;
        } while (i++ != END);

        System.out.println(count);
    }
}
