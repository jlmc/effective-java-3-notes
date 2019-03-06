package io.costax.serialization.basic;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SimpleSerialization {

    private static final String FILE_BOOK_SER = "book.ser";

    @Test
    public void serialization() throws IOException, ClassNotFoundException {
        final Book book = Book.createBook("isbn-1", "simple-serialization");

        serialize(FILE_BOOK_SER, book);

        System.out.println("**** < Deserialization ***");

        final Book deserializeBook = deserialize(Book.class, FILE_BOOK_SER);

        Assert.assertNotNull(deserializeBook);
        Assert.assertNotSame(book, deserializeBook);
        Assert.assertEquals(book, deserializeBook);
        Assert.assertEquals("isbn-1", deserializeBook.getIsbn());
        Assert.assertEquals("simple-serialization", deserializeBook.getTitle());
    }

    private void serialize(final String fileBookSer, final Book book) throws IOException {
        try (
                FileOutputStream file = new FileOutputStream(fileBookSer);
                ObjectOutputStream out = new ObjectOutputStream(file)
        ) {

            out.writeObject(book);

        }
    }

    private <T> T deserialize(Class<T> type, final String fileBookSer) throws IOException, ClassNotFoundException {
        try (
                FileInputStream file = new FileInputStream(fileBookSer);
                ObjectInputStream in = new ObjectInputStream(file)
        ) {
            final Object o = in.readObject();
            return type.cast(o);
        }
    }
}