package io.costax.item13.entity;

import io.costax.item13.entity.heritance.Car;
import io.costax.item13.entity.heritance.Owner;
import org.junit.Assert;
import org.junit.Test;

/**
 *  x.clone() != x ==> TRUE
 *
 *  x.clone().getClass() ==> x.getClass() ==> TRUE
 *
 *  x.clone().equals(x) ==> True (good), but not absolute required
 *
 *
 *  <p>
 *      Immutable classes should no be cloneable, they should never provide a clone method
 *  </p>
 *
 *  <p>
 *      Singletons should not be cloneable.
 *  </p>
 */
public class CloningTest {

    @Test(expected = java.lang.CloneNotSupportedException.class)
    public void shouldNotBeCloneable() throws CloneNotSupportedException {
        final PersonNoClonable x = PersonNoClonable.of("noClonable");
        final PersonNoClonable clone = (PersonNoClonable) x.clone();
    }

    @Test
    public void shouldCloneAndCast() throws CloneNotSupportedException {
        final PersonClonable x = PersonClonable.of("abcd");

        final PersonClonable clone = (PersonClonable) x.clone();

        Assert.assertTrue(x != clone);
        Assert.assertTrue(x.getClass() == clone.getClass());
        // will be false until we override the equals methods
        Assert.assertFalse(x.clone().equals(clone));
    }


    @Test
    public void shouldClone() {
        final PersonClonableNoCasts x = PersonClonableNoCasts.of("abcd");

        final PersonClonableNoCasts clone =  x.clone();

        Assert.assertTrue(x != clone);
        Assert.assertTrue(x.getClass() == clone.getClass());
        // will be false until we override the equals methods
        Assert.assertTrue(x.clone().equals(clone));
    }

    @Test
    public void heritance() {
        Car x = new Car(123, "AB-12-67", Owner.of("1234", "Costax"));

        final Car clone = x.clone();

        Assert.assertTrue(x != clone);
        Assert.assertTrue(x.getClass() == clone.getClass());
        // will be false until we override the equals methods
        Assert.assertTrue(x.clone().equals(clone));


        // only if the Owner class implements cloneable
        // we have to clone the owner in clone method of the Car
        Assert.assertTrue(x.getOwner() != clone.getOwner());
        Assert.assertTrue(x.getOwner().getClass() == clone.getOwner().getClass());
        Assert.assertTrue(x.getOwner().equals(clone.getOwner()));
    }
}
