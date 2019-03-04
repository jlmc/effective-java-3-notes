package io.costax.item13.entity;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack implements Cloneable {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Object[] elements;
    private int size = 0;

    public Stack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        Object result = elements[--size];
        elements[size] = null; //eliminate obsolete references
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    /**
     * Creates a shallow copy of this Stack. All the structure of the
     * Stack itself is copied, but the elements values are not cloned.
     * This is a relatively expensive operation.
     *
     * @return a clone of the Stack
     */
    @Override
    public Stack clone() {
        try {
            Stack clone = (Stack) super.clone();
            clone.elements = elements.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
