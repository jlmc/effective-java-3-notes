package io.costax.xtra;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    private String isbn;
    private String title;

    Book() {
        // Jax-b requires a not private no-arg default constructor.
    }

    public Book(final String isbn, final String title) {
        this.isbn = isbn;
        this.title = title;
    }

    // Omitted getters and setters
}
