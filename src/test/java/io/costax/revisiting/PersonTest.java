package io.costax.revisiting;

import org.junit.Assert;
import org.junit.Test;


public class PersonTest {

    @Test
    public void shouldUseABuilderToInstatiate() {
        final Person martinFowler = Person.builder("martin", "fowler")
                .title("Eng.")
                .build();

        Assert.assertNotNull(martinFowler);
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionBecauseAPersonShouldContainsAPrefixOrATitle() {
        final Person martinFowler = Person.builder("martin", "fowler")
                .build();
    }
}