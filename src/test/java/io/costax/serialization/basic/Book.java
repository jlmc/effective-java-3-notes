package io.costax.serialization.basic;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.*;
import java.util.Objects;

public class Book implements Serializable, ObjectInputValidation {

    private String isbn;
    private String title;

    private Book(final String isbn, final String title) {
        this.isbn = isbn;
        this.title = title;
    }

    public static Book createBook(final String isbn, final String title) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(isbn), "isbn should be not blank");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(title), "title should be not blank");

        return new Book(isbn, title);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        final Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }


    //********* Serialization Magic methods ********* */

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        System.out.println("writeObject");
        out.defaultWriteObject();
    }

    private Object writeReplace() throws ObjectStreamException {
        System.out.println("writeReplace");
        return this;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.out.println("readObject");
        in.registerValidation(this, 0);
        in.defaultReadObject();
    }

    @Override
    public void validateObject() throws InvalidObjectException {
        System.out.println("validateObject");
    }

    private Object readResolve() throws ObjectStreamException {
        System.out.println("readResolve");
        return this;
    }
}
