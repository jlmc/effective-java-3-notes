package io.costax.item13.entity;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StackTest {

    @Test
    public void testSkackCloneMethod() {

        final Stack x = new Stack();
        final PhoneNumber pn = PhoneNumber.from((short) 1, (short) 2, (short) 3);
        x.push(pn);

        final Stack clone = x.clone();

        assertTrue(clone != x);
        assertTrue(x.getClass() == clone.getClass());
        //assertTrue(x.equals(clone)); // stack do not implements equals methods
        final Object pop = clone.pop();
        assertTrue(pop != pn);

    }
}