package io.costax.item17.minimizemutability;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {


    @Test
    @DisplayName("Assert that the instantiation  use the cached value")
    void createComplexNumber() {
        final ComplexNumber one = ComplexNumber.valueOf(1.0D, 0.0D);

        assertSame(ComplexNumber.ONE, one);
    }

}