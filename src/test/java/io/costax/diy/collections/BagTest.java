package io.costax.diy.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BagTest {

    @Test
    public void countLetters() {
        String testString = "the quick brown fox jumps over the lazy dog";
        Map<String, Long> map = testString.chars()
                .filter(Character::isLetter)
                .mapToObj(each -> String.valueOf((char) each))
                .collect(Collectors.groupingBy(each -> each, Collectors.counting()));


        assertEquals(26, map.size());
        assertEquals(35,
                map.values().stream().mapToInt(Long::intValue).sum());

    }

    @Test
    public void createBag() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange, apple);

        assertEquals(2, bag.getOccurrences(apple));
        assertEquals(1, bag.getOccurrences(banana));
        assertEquals(1, bag.getOccurrences(orange));
        assertEquals(4, bag.size());
        assertEquals(3, bag.sizeDistinct());
    }

    @Test
    public void addOccurrencesBag() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange, orange, orange, apple);

        assertEquals(2, bag.getOccurrences(apple));
        assertEquals(1, bag.getOccurrences(banana));
        assertEquals(3, bag.getOccurrences(orange));
        assertEquals(6, bag.size());
        assertEquals(3, bag.sizeDistinct());

        bag.addOccurrences(orange, 4);
        bag.addOccurrences("strawberry", 12);
        assertEquals(7, bag.getOccurrences(orange));
        assertEquals(12, bag.getOccurrences("strawberry"));
        assertEquals(22, bag.size());
        assertEquals(4, bag.sizeDistinct());

        bag.addOccurrence("strawberry");
        assertEquals(13, bag.getOccurrences("strawberry"));
    }

    @Test
    public void removeOccurrencesBag() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange, orange, orange, apple);

        assertEquals(2, bag.getOccurrences(apple));
        assertEquals(1, bag.getOccurrences(banana));
        assertEquals(3, bag.getOccurrences(orange));
        assertEquals(6, bag.size());
        assertEquals(3, bag.sizeDistinct());

        bag.removeOccurrence(orange);
        assertEquals(2, bag.getOccurrences(orange));
        assertEquals(5, bag.size());
        assertEquals(3, bag.sizeDistinct());

        bag.removeOccurrence(orange);
        bag.removeOccurrence(orange);
        bag.removeOccurrence(orange);

        assertEquals(0, bag.getOccurrences(orange));
        assertEquals(3, bag.size());
        assertEquals(2, bag.sizeDistinct());
    }

    @Test
    public void removeOccurrencesBag2() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange, orange, orange, apple);

        assertEquals(2, bag.getOccurrences(apple));
        assertEquals(1, bag.getOccurrences(banana));
        assertEquals(3, bag.getOccurrences(orange));
        assertEquals(6, bag.size());
        assertEquals(3, bag.sizeDistinct());

        bag.removeOccurrences(orange, 4);
        assertEquals(0, bag.getOccurrences(orange));
        assertEquals(3, bag.size());
        assertEquals(2, bag.sizeDistinct());
    }

    @Test
    public void forEachWithOccurrences() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange);

        StringBuilder builder = new StringBuilder();

        bag.forEachWithOccurrences((each, occurrences)
                -> builder.append(each).append(":").append(occurrences));
        assertTrue(builder.toString().contains("apple:1"));
    }

    @Test
    public void forEach() {
        String apple = "apple";
        String banana = "banana";
        String orange = "orange";

        Bag<String> bag = new Bag(apple, banana, orange, apple);

        StringBuilder builder = new StringBuilder();

        bag.forEach(each -> builder.append(each).append(", "));
        assertTrue(builder.toString().contains("apple, apple"));
        assertTrue(builder.toString().contains("banana, "));
    }

}