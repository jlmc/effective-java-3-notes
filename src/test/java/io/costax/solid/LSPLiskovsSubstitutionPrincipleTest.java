package io.costax.solid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

@DisplayName("LSP - Liskov's Substitution Principle")
public class LSPLiskovsSubstitutionPrincipleTest {

    @Nested
    class GoodEvidences {
        @Test
        void methodVisibility() throws IOException {
            Engine engine = new ElectricEngine();
            engine.shutdown();
        }

        @Test
        void executeProcess() {
            final List<Book> books = new ArrayList<>();
            BigDecimal process = process(books);

            final List<TechBook> techBooks = new ArrayList<>();
            //process(techBooks); // compilation error: Error:(45, 21) java: incompatible types: java.util.List<io.costax.solid.LSPLiskovsSubstitutionPrincipleTest.GoodEvidences.TechBook> cannot be converted to java.util.List<io.costax.solid.LSPLiskovsSubstitutionPrincipleTest.GoodEvidences.Book>
        }
        //@formatter:on

        BigDecimal process(List<Book> books) {
            return books
                    .stream()
                    .map(Book::price)
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add);
        }

        //@formatter:off
        class Engine {
            protected void shutdown() throws IOException { System.out.println("--- ** ");}
        }

        class ElectricEngine  extends Engine {
            @Override public void shutdown() {  System.out.println("--- ** > electric"); }
        }
        //@formatter:on

        //@formatter:off
        class Book { public BigDecimal price() { return BigDecimal.TEN; } }

        class TechBook extends Book {  public  BigDecimal price() { return super.price().add(BigDecimal.ONE); } }
    }


    @Nested
    @DisplayName("Violation of LSP In JVM")
    class ExampleOfViolationInJVM {

        @Test
        void badSubstitutionExample() {
            Vector<String> vector = new Vector<>();
            vector.add("A");
            vector.add("B");
            vector.add("C");
            addAndRemoveSomeItens(vector);

            System.out.println(vector);
            Assertions.assertEquals(vector.size(), 4);
            Assertions.assertEquals("A", vector.get(0));
            Assertions.assertEquals("B", vector.get(1));
            Assertions.assertEquals("C", vector.get(2));
            Assertions.assertEquals("D", vector.get(3));

            Vector<String> stack = new Stack<>();
            stack.add("A");
            stack.add("B");
            stack.add("C");
            addAndRemoveSomeItens(stack);
            System.out.println(vector);
        }

        private void addAndRemoveSomeItens(final Vector<String> vector) {
            vector.add("D");
            vector.add("E");
            vector.add("F");

            vector.remove(vector.size() - 1);
            vector.remove(vector.size() - 1);
        }
    }
}
