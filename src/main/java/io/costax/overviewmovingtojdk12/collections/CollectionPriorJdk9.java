package io.costax.overviewmovingtojdk12.collections;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionPriorJdk9 {

    public static void main(String[] args) {
        // create a immutable list
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list = Collections.unmodifiableList(list);

        /* if we add list.add(4); then we will have the output
         * Exception in thread "main" java.lang.UnsupportedOperationException
         * 	at java.base/java.util.Collections$UnmodifiableCollection.add(Collections.java:1058)
         * 	at io.costax.overviewmovingtojdk12.collections.CollectionPriorJdk9.main(CollectionPriorJdk9.java:23)
         */
        System.out.println(list);

        //1.
        // this isn't a truly immutable collection, that's more like an immutable view of the underlying collection
        // but if we keep the references to the original list we would skill be able to modify it.
        // This is why this idiom is very common we have read it in unmodifiable and then we assign it to the same variable.
        //
        // 2. We have also the performance overhead.
        // because if we really need a immutable list we need also to duplicate all the elements

        // ****************************************
        // other alternative

        List<Integer> list2 = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(1, 2, 3)));

        // ****************************************
        // other alternative using stream api
        List<Integer> list3 = Stream.of(1, 2, 3)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::unmodifiableList));

        // ****************************************
        // The horst alternative  possible
        // This approach have a very high costs because we are creating a anonymous class that extends from the arraysList
        final List<Integer> integers = Collections.unmodifiableList(
                new ArrayList<>() {
                    {
                        add(1);
                        add(2);
                        add(3);
                    }
                });

        // ****************************************
        // we can use a extra library to do what is really missing

        // using jshell
        // /env -class-path guava-27.1-jre.jar
        // import com.google.common.collect.ImmutableList


        List<Integer> of = ImmutableList.of(1, 2, 3);

        // this a very good approach, we have a single line of code and the result is a really immutable list

        // of.add(4);
        // Exception in thread "main" java.lang.UnsupportedOperationException
        //	at com.google.common.collect.ImmutableCollection.add(ImmutableCollection.java:201)
        //	at io.costax.overviewmovingtojdk12.collections.CollectionPriorJdk9.main(CollectionPriorJdk9.java:81)
    }


}
