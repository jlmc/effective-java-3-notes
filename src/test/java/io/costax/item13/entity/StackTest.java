package io.costax.item13.entity;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class StackTest {

    @Test
    public void testSkackCloneMethod() {

        final Stack stack = new Stack();

        final PhoneNumber phoneNumber = PhoneNumber.from((short) 1, (short) 2, (short) 3);
        stack.push(phoneNumber);

        final Stack clone = stack.clone();

        assertNotSame(clone, stack);
        assertSame(stack.getClass(), clone.getClass());
        //assertTrue(x.equals(clone)); // stack do not implements equals methods
        final Object pop = clone.pop();

        // prove that there are not deep copy o the references objects
        assertSame(pop, phoneNumber);

    }
}