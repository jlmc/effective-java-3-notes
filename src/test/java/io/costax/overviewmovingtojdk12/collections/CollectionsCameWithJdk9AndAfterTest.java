package io.costax.overviewmovingtojdk12.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsCameWithJdk9AndAfterTest {


    @Test
    @DisplayName("create immutable list using jdk 9")
    void create_immutable_using_jdk_9() {
        final List<Integer> immutablelist = List.of(1, 2, 3);

        assertNotNull(immutablelist);
        assertThrows(java.lang.UnsupportedOperationException.class, () -> immutablelist.add(4));
        assertThrows(java.lang.UnsupportedOperationException.class, () -> immutablelist.remove(1));

        final Class<? extends List> aClass = immutablelist.getClass();
        assertTrue(aClass.getName().equals("java.util.ImmutableCollections$ListN"));
        // java.util.ImmutableCollections$ListN is a internal class of JDK there is way instantiate in other form
    }

    @Test
    @DisplayName("create immutable list using jdk 10")
    void create_immutable_copy_using_jdk_10() {
        final List<Integer> immutablelist = List.of(1, 2, 3);

        final List<Integer> copyOfImmutablelist = List.copyOf(immutablelist);

        assertThrows(java.lang.UnsupportedOperationException.class, () -> copyOfImmutablelist.add(4));
        assertThrows(java.lang.UnsupportedOperationException.class, () -> copyOfImmutablelist.remove(1));
        final Class<? extends List> aClass = copyOfImmutablelist.getClass();
        assertTrue(aClass.getName().equals("java.util.ImmutableCollections$ListN"));
    }

    @Test
    void create_immutable_map() {
        final Map<Integer, String> integerStringMap = Map.of(1, "A", 2, "B");

        assertThrows(java.lang.UnsupportedOperationException.class, () -> integerStringMap.put(3, "C"));
    }

    @Test
    void using_Unmodifiable_collectors_with_jdk_10() {
        final List<Integer> collect = List.of(1, 2, 3, 4).stream().collect(Collectors.toUnmodifiableList());

        final ArrayList<String> names = new ArrayList<>();
        names.add("mario");
        names.add("ricardo");
        names.add("nuno");

        final String[] namesAsArray = names.toArray(String[]::new);


    }
}
