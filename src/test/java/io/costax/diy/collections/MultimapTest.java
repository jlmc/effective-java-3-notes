package io.costax.diy.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class MultimapTest {

    @Test
    public void create() {
        SetMultimap<Object, Object> setMultimap = new SetMultimap<>();
        ListMultimap<Object, Object> listMultimap = new ListMultimap<>();
        Assertions.assertNotNull(setMultimap);
        Assertions.assertNotNull(listMultimap);
    }

    // putSetMultimap
    @Test
    public void putSetMultimap() {
        Multimap<String, String> setMultimap = new SetMultimap<>();

        setMultimap.put("Animal", "Cat");

        Assertions.assertEquals(
                Set.of("Cat"),
                setMultimap.get("Animal"));

        setMultimap.put("Animal", "Dog");
        setMultimap.put("Animal", "Elephant");

        Assertions.assertEquals(
                Set.of("Cat", "Dog", "Elephant"),
                setMultimap.get("Animal"));

        setMultimap.put("Animal", "Dog");
        Assertions.assertEquals(
                Set.of("Cat", "Dog", "Elephant"),
                setMultimap.get("Animal"));

        setMultimap.putAll("Animal", List.of("Dog", "Cat", "Dog", "Monkey"));
        Assertions.assertEquals(
                Set.of("Cat", "Dog", "Elephant", "Monkey"),
                setMultimap.get("Animal"));
    }

    // putListMultimap
    @Test
    public void putListMultimap() {
        ListMultimap<String, String> listMultimap = new ListMultimap<>();

        listMultimap.put("Animal", "Cat");
        Assertions.assertEquals(
                List.of("Cat"),
                listMultimap.get("Animal"));

        listMultimap.put("Animal", "Dog");
        listMultimap.put("Animal", "Elephant");
        Assertions.assertEquals(
                List.of("Cat", "Dog", "Elephant"),
                listMultimap.get("Animal"));

        listMultimap.put("Animal", "Dog");
        Assertions.assertEquals(
                List.of("Cat", "Dog", "Elephant", "Dog"),
                listMultimap.get("Animal"));

        listMultimap.putAll("Animal", List.of("Dog", "Cat", "Monkey"));
        Assertions.assertEquals(
                List.of("Cat", "Dog", "Elephant", "Dog", "Dog", "Cat", "Monkey"),
                listMultimap.get("Animal"));
    }

    @Test
    public void removeSetMultimap() {
        SetMultimap<String, String> setMultimap = new SetMultimap<>();
        setMultimap.put("Animal", "Cat");
        setMultimap.put("Animal", "Dog");
        setMultimap.put("Animal", "Elephant");
        setMultimap.put("Animal", "Dog");
        Assertions.assertEquals(
                Set.of("Cat", "Dog", "Elephant"),
                setMultimap.get("Animal"));

        setMultimap.remove("Animal", "Dog");
        Assertions.assertEquals(Set.of("Cat", "Elephant"), setMultimap.get("Animal"));

        setMultimap.removeAll("Animal");
        Assertions.assertEquals(Set.of(), setMultimap.get("Animal"));
    }

    @Test
    public void removeListMultimap() {
        ListMultimap<String, String> listMultimap = new ListMultimap<>();
        listMultimap.put("Animal", "Cat");
        listMultimap.put("Animal", "Dog");
        listMultimap.put("Animal", "Elephant");
        listMultimap.put("Animal", "Dog");
        Assertions.assertEquals(
                List.of("Cat", "Dog", "Elephant", "Dog"),
                listMultimap.get("Animal"));

        listMultimap.remove("Animal", "Dog");
        Assertions.assertEquals(
                List.of("Cat", "Elephant", "Dog"),
                listMultimap.get("Animal"));

        listMultimap.remove("Animal", "Dog");
        Assertions.assertEquals(List.of("Cat", "Elephant"), listMultimap.get("Animal"));

        listMultimap.removeAll("Animal");
        Assertions.assertEquals(List.of(), listMultimap.get("Animal"));
    }

}