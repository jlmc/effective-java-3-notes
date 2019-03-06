package io.costax.puzzlers;

import org.junit.Test;

/**
 * The lesson is simple: If you can’t tell what a program does by looking at it,
 * it probably doesn’t do what you want
 */
public class Multicast {

    @Test
    public void multicast() {
        System.out.println((int) (char) (byte) -1);
    }

    /*
     * It prints 65535, but why?
     * The program’s behavior depends critically on the sign extension behavior of
     * casts.
     *
     *  - Java uses two’s-complement binary arithmetic, so the int value -1 has all 32 bits set.
     *
     *  - The cast from int to byte is straightforward. It performs a narrowing primitive conversion [JLS 5.1.3],
     *   which simply lops off all but the low-order 8 bits.
     *
     *  - This leaves a byte value with all 8 bits set, which (still) represents 1.
     *
     * - The cast from byte to char is trickier because byte is a signed type and char unsigned.
     *  It is usually possible to convert from one integral type to a wider one  while preserving numerical value,
     *  but it is impossible to represent a negative byte value as a char.
     */


    /**
     * <pre>
     *
     *
     * If you are converting from a char value c to a wider type and you don’t want
     * sign extension, consider using a bit mask for clarity, even though it isn’t required:
     *
     *  <code>
     *  int i = c & 0xffff;
     *  </code>
     *
     * Alternatively, write a comment describing the behavior of the conversion:
     *
     * <code>
     *  int i = c; // Sign extension is not performed
     * </code>
     *
     * If you are converting from a char value c to a wider integral type and you
     * want sign extension, cast the char to a short, which is the same width as a char
     * but signed. Given the subtlety of this code, you should also write a comment:
     *
     * <code>
     * int i = (short) c; // Cast causes sign extension
     * </code>
     *
     * If you are converting from a byte value b to a char and you don’t want sign
     * extension, you must use a bit mask to suppress it. This is a common idiom, so no
     * comment is necessary:
     *
     * <code>
     * char c = (char) (b & 0xff);
     * </code>
     *
     *
     * If you are converting from a byte to a char and you want sign extension, write a comment:
     * char c = (char) b; // Sign extension is performed
     * </pre>
     */
    @Test
    public void solution() {
        int i = -1;
        byte b = (byte) i;
        char c = (char) (b & 0xff); // Sign extension is not performed
        int ii = (short) c; // Cast causes sign extension

        System.out.println(ii);
    }
}
