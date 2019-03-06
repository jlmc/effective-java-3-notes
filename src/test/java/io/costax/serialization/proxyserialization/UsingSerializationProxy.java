package io.costax.serialization.proxyserialization;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Item 90: Consider serialization proxies instead of
 * serialized instances
 */
public class UsingSerializationProxy {

    @Test
    public void usingProxy() throws IOException, ClassNotFoundException {
        final ComplexNumber src = ComplexNumber.of(88.5d, 12.9d, 1.0, 2.0);
        System.out.println(src);

        serialize("complex-number.ser", src);

        System.out.println("*** Now Get Back the src from the file");

        final ComplexNumber deserialize = deserialize(ComplexNumber.class, "complex-number.ser");

        System.out.println(deserialize);

        Assert.assertEquals(src.getReal(), deserialize.getReal(), 0.0D);
        Assert.assertEquals(src.getImaginary(), deserialize.getImaginary(), 0.0D);
        Assert.assertNotEquals(src.getAngle(), deserialize.getAngle(), 0.009D);
        Assert.assertNotEquals(src.getMagnitude(), deserialize.getMagnitude(), 0.9D);
    }

    private void serialize(final String filePath, final Object o) throws IOException {
        try (
                FileOutputStream file = new FileOutputStream(filePath);
                ObjectOutputStream out = new ObjectOutputStream(file)
        ) {

            out.writeObject(o);

        }
    }

    private <T> T deserialize(Class<T> type, final String filePath) throws IOException, ClassNotFoundException {
        try (
                FileInputStream file = new FileInputStream(filePath);
                ObjectInputStream in = new ObjectInputStream(file)
        ) {
            final Object o = in.readObject();
            return type.cast(o);
        }
    }
}
