package io.costax.chapter7;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.stream.Collectors.*;

class StreamCollectorsTest {

    private static final Consumer<Object> PRINTLN = out::println;
    private static final BiConsumer<Object, Object> BI_PRINTLN = (k, v) -> out.printf("%s - %s\n", k, v);

    static List<Person> createPeople() {
        return List.of(
                new Person("Sara", 20),
                new Person("Sara", 22),
                new Person("Bob", 20),
                new Person("Paula", 32),
                new Person("Paul", 32),
                new Person("Jack", 3),
                new Person("Jack", 72),
                new Person("Jill", 11)
        );
    }

    static List<Person> createPeople2() {
        return List.of(
                new Person("Sara", 20),
                new Person("Sara", 22),
                new Person("Bob", 20),
                new Person("Paula", 32),
                new Person("Paul", 32),
                new Person("Jack", 3),
                new Person("Jack", 72),
                new Person("jACK", 72),
                new Person("Jill", 11)
        );
    }

    static List<Person> createPeopleWithNoDuplicates() {
        return List.of(
                new Person("Sara", 20),
                new Person("Nancy", 22),
                new Person("Bob", 20),
                new Person("Paula", 32),
                new Person("Paul", 32),
                new Person("Jack", 3),
                new Person("Bill", 72),
                new Person("Jill", 11)
        );
    }

    @Test
    void simpleUseStreams() {
        createPeople().forEach(out::println);
    }

    @Test
    void simple2() {
        createPeople()
                .stream()
                .filter(person -> person.getAge() > 30)
                .forEach(PRINTLN);
    }

    @Test
    void simple3() {
        createPeople()
                .stream()
                .map(Person::getName)
                .forEach(PRINTLN);
    }

    @Test
    void simple4() {
        final Integer sumAllAges = createPeople()
                .stream()
                .map(Person::getAge)
                //.reduce(0, (total, age) -> total + age);
                // reduce converts a Stream to something concrete
                .reduce(0, Integer::sum);

        out.println(sumAllAges);
    }

    @Test
    void simple5() {
        // Java has reduce in two forms:
        // reduce and collect

        // OOP Object-Oriented Programming: Polymorphism

        // Functional Programming: Functional Composition + lazy evaluation

        // Pure function returns the same result any number of times we call it with same input - idempotency
        // 1. Pure function don't have side-effects
        // 2. Pure function do not change anything
        // 3. Pure function do not depend on anything that may possible change
        // So, immutable is key factor fo success using functional programming

        // What to deal with exceptions?
        // Exceptions handing is an imperative style of programming concept
        // In Functions programming we deal with Stream of data - dataflow
        // So, Exceptions are also data, should be treated as error, so we should processing also as data in functional Programming
        // transforming and filtering

        // example:
        // get the list of names, in uppercase, of those who are older that 30

        // Don't do this: name is not a pure functions, its could depends of any thing else
        //    -  we can do this in parallel
       /* final List<String> names0 = new ArrayList<>();
        createPeople()
                .stream()
                .filter(person -> person.getAge() > 30)
                .map(Person::getName)
                .map(String::toUpperCase)
                .forEach(name -> names0.add(name));*/

        ArrayList<String> reduces = createPeople()
                .stream()
                .filter(person -> person.getAge() > 30)
                .map(Person::getName)
                .map(String::toUpperCase)
                .reduce(
                        new ArrayList<>(),
                        new BiFunction<ArrayList<String>, String, ArrayList<String>>() {
                            @Override
                            public ArrayList<String> apply(final ArrayList<String> names, final String name) {
                                names.add(name);
                                return names;
                            }
                        },
                        new BinaryOperator<ArrayList<String>>() {
                            @Override
                            public ArrayList<String> apply(final ArrayList<String> names1, final ArrayList<String> names2) {
                                names1.addAll(names2);
                                return names1;
                            }
                        }
                );

        final List<String> names = createPeople()
                .stream()
                .filter(person -> person.getAge() > 30)
                .map(Person::getName)
                .map(String::toUpperCase)
                .collect(toUnmodifiableList());

        names.forEach(PRINTLN);
    }

    @Test
    void sample6() {
        // name as key and age as value
        Map<String, Integer> map = createPeopleWithNoDuplicates()
                .stream()
                .collect(toUnmodifiableMap(Person::getName, Person::getAge));

        map.forEach(BI_PRINTLN);
    }

    @Test
    void simple7() {
        // create comma separated the name in uppercase of people old than 30

        String collect = createPeople()
                .stream()
                .filter(person -> person.getAge() > 30)
                .map(Person::getName)
                .map(String::toUpperCase)
                .collect(joining(", "));

        out.println(collect);
    }

    @Test
    void simple8() {

        // group people by even age and not age

      /*  List<Person> evenAged = createPeople()
                .stream()
                .filter(person -> person.getAge() % 2 == 0)
                .collect(toUnmodifiableList());

        List<Person> oddAged = createPeople()
                .stream()
                .filter(person -> person.getAge() % 2 != 0)
                .collect(toUnmodifiableList());*/


        // the true contains all the eval ages persons and the false contains all the odd age persons
        Map<Boolean, List<Person>> collect = createPeople()
                .stream()
                .collect(partitioningBy(person -> person.getAge() % 2 == 0));

        collect.forEach(BI_PRINTLN);

        Map<Boolean, List<String>> collect1 = createPeople()
                .stream()
                .collect(
                        partitioningBy(
                                person -> person.getAge() % 2 == 0,
                                mapping(Person::getName, toUnmodifiableList())));

        collect1.forEach(BI_PRINTLN);
    }

    @Test
    void sample9() {
        // group the people based on their name
        Map<String, List<Person>> collect = createPeople()
                .stream()
                .collect(groupingBy(Person::getName));

        collect.forEach(BI_PRINTLN);
    }

    @Test
    void sample10() {
        Map<String, List<Integer>> collect = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getName,
                                mapping(Person::getAge,
                                        toUnmodifiableList())));

        collect.forEach(BI_PRINTLN);
    }

    @Test
    void sample11() {
        // count the number of each names happens
        Map<String, Long> collect = createPeople()
                .stream()
                .collect(groupingBy(Person::getName, counting()));


        Map<String, Long> collect1 = createPeople2()
                .stream()
                .collect(
                        groupingBy(person -> person.getName().toUpperCase(), counting()));

        collect1.forEach(BI_PRINTLN);
    }

    @Test
    void sample12() {
        final Collector<Person, ?, Integer> reducingAsInt = reducing(0, person -> 1, Integer::sum);
        // count the number of each names happens
        Map<String, Integer> collect = createPeople()
                .stream()
                .collect(groupingBy(Person::getName, reducingAsInt));

        collect.forEach(BI_PRINTLN);
    }

    @Test
    void sample13() {
        Map<String, Integer> collect = createPeople()
                .stream()
                .collect(groupingBy(Person::getName, collectingAndThen(counting(), Long::intValue)));

        collect.forEach(BI_PRINTLN);

        out.println("----");

        Map<String, Integer> collect1 = createPeople()
                .stream()
                .collect(groupingBy(Person::getName, summingInt(p -> 1)));

        collect1.forEach(BI_PRINTLN);
    }

    @Test
    void sample14() {
        //maxBy

        OptionalInt max = createPeople()
                .stream()
                .mapToInt(Person::getAge)
                .max();

        out.println(max);
    }

    @Test
    void sample15() {
        OptionalInt min = createPeople()
                .stream()
                .mapToInt(Person::getAge)
                .min();

        out.println(min);
    }

    @Test
    void sample16() {
            /*        
            Person person = createPeople()
                .stream()
                .collect(maxBy(Comparator.comparing(Person::getAge)))
                .orElse(null);*/

        Person person = createPeople()
                .stream()
                .max(Comparator.comparing(Person::getAge))
                .orElse(null);

        out.println(person);
    }

    @Test
    void sample17() {
        String collect = createPeople()
                .stream()
                .collect(
                        collectingAndThen(
                                maxBy(Comparator.comparing(Person::getAge)),
                                personOptional -> personOptional.map(Person::getName).orElse("")));

        out.println(collect);
    }

    @Test
    void sample18() {
        Map<Integer, List<String>> collect = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getAge,
                                mapping(Person::getName,
                                        filtering(name -> name.length() > 4,
                                                toUnmodifiableList()))));

        collect.forEach(BI_PRINTLN);
    }

    @Test
    void sample19() {
        // teeing
        // teeing(Collector, Collector, operation)

        Map<String, Person> collect = createPeople()
                .stream()
                .collect(
                        teeing(
                                maxBy(Comparator.comparing(Person::getAge)),
                                minBy(Comparator.comparing(Person::getAge)),
                                (max, min) -> Map.of("theMax", max.orElse(null), "theMin", min.orElse(null))
                        )
                );

        collect.forEach(BI_PRINTLN);
    }

    @Test
    void sample20() {
        List<Integer> numbers = List.of(1, 2, 3, 4);

        List<List<Integer>> collect = numbers
                .stream()
                .map(e -> List.of(e - 1, e + 1))
                .collect(toList());

        List<Stream<Integer>> collect1 = numbers
                .stream()
                .map(e -> List.of(e - 1, e + 1).stream())
                .collect(toList());

        List<Integer> collect2 = numbers
                .stream()
                .flatMap(e -> List.of(e - 1, e + 1).stream())
                .collect(toList());

        out.println(collect2);
    }

    @Test
    void sample21() {

        List<String> collect = createPeople()
                .stream()
                .map(Person::getName)
                .flatMap(name -> Stream.of(name.split("")))
                .collect(toUnmodifiableList());

        out.println(collect);
    }

    @Test
    void sample22() {
        /*
        Map<Integer, List<Stream<String>>> collect = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getAge,
                                mapping(

                                        person -> Stream.of(person.getName().split("")),
                                        toList()
                                )
                        )
                );
        */

        Map<Integer, List<String>> collect = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getAge,
                                flatMapping(

                                        person -> Stream.of(person.getName().split("")),
                                        toList()
                                )
                        )

                );

        Map<Integer, Set<String>> collect1 = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getAge,
                                flatMapping(

                                        person -> Stream.of(person.getName().split("")),
                                        toSet()
                                )
                        ));

        final Map<Integer, Set<String>> collect2 = createPeople()
                .stream()
                .collect(
                        groupingBy(
                                Person::getAge,
                                mapping(
                                        person -> person.getName().toUpperCase(),
                                        flatMapping(mameUpperCase -> Stream.of(mameUpperCase.split("")), toSet())
                                )
                        )
                );

        out.println(collect2);
    }

    public static class Person {
        private final String name;
        private final Integer age;

        public Person(final String name, final Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() +
                    " name='" + name + '\'' +
                    ", age=" + age
                    ;
        }
    }

}