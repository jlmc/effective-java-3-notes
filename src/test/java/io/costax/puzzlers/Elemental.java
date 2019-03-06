package io.costax.puzzlers;

import org.junit.Test;

/**
 * <pre>
 * Always use a capital el (L) in long literals, never a lowercase el (l).
 * Avoid using a lone el (l) as a variable name.
 *
 * It is difficult to tell by looking at this code snippet whether it prints the list l or the number 1:
 * </pre>
 */
public class Elemental {

    @Test
    public void elemental() {
        // What is the output
        System.out.println(12345 + 5432l);
    }
}
