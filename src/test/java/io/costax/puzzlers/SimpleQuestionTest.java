package io.costax.puzzlers;

import org.junit.Assert;
import org.junit.Test;

import static io.costax.puzzlers.SimpleQuestion.yesOrNo;
import static io.costax.puzzlers.SimpleQuestion.yesOrNoClearAndReadable;

public class SimpleQuestionTest {

    @Test
    public void yesOrNoTest() {
        boolean value = yesOrNo("true");
        boolean value2 = yesOrNo("YeS");

        // false false
        System.out.println(value + " " + value2);
        Assert.assertEquals("false false", value + " " + value2);
    }

    @Test
    public void yesOrNoClearAndReadableTest() {
        Assert.assertEquals("true true", yesOrNoClearAndReadable("true") + " " + yesOrNoClearAndReadable("YeS"));
    }
}