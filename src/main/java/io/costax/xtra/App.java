package io.costax.xtra;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class App {

    public static void main(String[] args) throws JAXBException {
        Book book = new Book("978-3-16-148410-0", "Jaxb after Java 8");

        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(book, System.out);
    }
}
