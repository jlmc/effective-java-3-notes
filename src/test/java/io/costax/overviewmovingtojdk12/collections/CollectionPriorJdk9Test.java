package io.costax.overviewmovingtojdk12.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectionPriorJdk9Test {

    /*
     * Using all the codes in jshell
     *
     *  env -class-path guava-27.1-jre.jar
     *  import com.google.common.collect.ImmutableList
     */

    @Test
    @DisplayName("create immutable list using jdk prior 9")
    void create_immutable_using_jdk() {

        //1.
        // this isn't a truly immutable collection, that's more like an immutable view of the underlying collection
        // but if we keep the references to the original list we would skill be able to modify it.
        // This is why this idiom is very common we have read it in unmodifiable and then we assign it to the same variable.
        //
        // 2. We have also the performance overhead.
        // because if we really need a immutable list we need also to duplicate all the elements

        // create a immutable list
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list = Collections.unmodifiableList(list);

        assertNotNull(list);
        final List<Integer> finalList = list;
        assertThrows(java.lang.UnsupportedOperationException.class, () -> finalList.add(4));
    }

    @Test
    @DisplayName("create immutable list using Arrays.asList nested in Collections.unmodifiableList")
    void create_unmodificable_list_using_arrays_as_list_nested_in_collections_unmodifiablelist() {

        List<Integer> list2 = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(1, 2, 3)));

        assertNotNull(list2);
        final List<Integer> finalList = list2;
        assertThrows(java.lang.UnsupportedOperationException.class, () -> finalList.add(4));
    }

    @Test
    @DisplayName("create immutable list using Stream.of nested in Collections.unmodifiableList")
    void create_unmodificableList_using_strem_of_nested_in_collections_unmodifiableList() {
        // other alternative using stream api
        List<Integer> list = Stream.of(1, 2, 3)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::unmodifiableList));

        assertNotNull(list);
        final List<Integer> finalList = list;
        assertThrows(java.lang.UnsupportedOperationException.class, () -> finalList.add(4));
    }

    @Test
    @DisplayName("create immutable list using  anonymous class that extends from the arraysList")
    void create_unmodificableList_using_anonymous_class_that_extends_from_the_arraysList() {

        // The horst alternative  possible
        final List<Integer> list = Collections.unmodifiableList(
                new ArrayList<>() {
                    {
                        add(1);
                        add(2);
                        add(3);
                    }
                });

        // This approach have a very high costs because we are creating a anonymous class that extends from the arraysList

        assertNotNull(list);
        final List<Integer> finalList = list;
        assertThrows(java.lang.UnsupportedOperationException.class, () -> finalList.add(4));
    }

    @Test
    @DisplayName("create immutable list using 3 part library")
    void create_immutable_list_using_3part_library() {

        final List<Integer> of = com.google.common.collect.ImmutableList.of(1, 2, 3);

        // this a very good approach, we have a single line of code and the result is a really immutable list

        assertThrows(java.lang.UnsupportedOperationException.class, () -> of.add(4));

        // of.add(4);
        // Exception in thread "main" java.lang.UnsupportedOperationException
        //	at com.google.common.collect.ImmutableCollection.add(ImmutableCollection.java:201)
        //	at io.costax.overviewmovingtojdk12.collections.CollectionPriorJdk9.main(CollectionPriorJdk9.java:81)
    }

    @Test
    @DisplayName("should not supports modifications")
    void should_not_add_in_unmodifiableList() {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List unmodifiableList = Collections.unmodifiableList(list);

        assertThrows(java.lang.UnsupportedOperationException.class, () -> {
            // create a immutable list

            /* if we add list.add(4); then we will have the output
             * Exception in thread "main" java.lang.UnsupportedOperationException
             * 	at java.base/java.util.Collections$UnmodifiableCollection.add(Collections.java:1058)
             * 	at io.costax.overviewmovingtojdk12.collections.CollectionPriorJdk9.main(CollectionPriorJdk9.java:23)
             */
            unmodifiableList.add(4);
        });

        assertThrows(java.lang.UnsupportedOperationException.class, () -> {
            unmodifiableList.remove(1);
        });
    }
}
