package io.costax.diy.collections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Bag (MultiSet): apples=5, bananas=4, oranges=6
 * <p>
 * The most common use case is the shopping cart.
 * <p>
 * Similar to a List where duplicates are allowed.
 * Similar to Set where elements that are retained are unique, the data structure is hashed.
 * Similar to Map where the element to the count is available.
 *
 * @see <a href="https://www.eclipse.org/collections/">Eclipse Collections</a>
 * @see <a href="https://github.com/eclipse/eclipse-collections">eclipse collections github</a>
 */
public class Bag<T> {

    private final Map<T, Integer> backingMap = new HashMap<>();
    private int size = 0;

    @SafeVarargs
    public Bag(T... elements) {
        this(Arrays.asList(elements));
    }

    private Bag(List<T> elements) {
        for (final T element : elements) {

            /*
            Integer existing = this.backingMap.get(element);
            if (existing != null) {
                existing++;
            } else {
                existing = 1;
            }
            this.backingMap.put(element, existing);
            this.size = this.size() + 1;
            */

            this.backingMap.merge(element, 1, Integer::sum);
            this.size = this.size() + 1;
        }
    }

    public int getOccurrences(final T element) {
        return this.backingMap.getOrDefault(element, 0);
    }

    public int size() {
        return this.size;
    }

    public int sizeDistinct() {
        return this.backingMap.keySet().size();
    }

    public int addOccurrences(final T element, final int occurrences) {
        this.size += occurrences;
        return this.backingMap.merge(element, occurrences, Integer::sum);
    }

    public int addOccurrence(final T element) {
        return this.addOccurrences(element, 1);
    }

    public boolean removeOccurrence(final T element) {
        return removeOccurrences(element, 1);
    }

    public boolean removeOccurrences(final T element, final int occurrences) {
        Integer existing = this.backingMap.get(element);
        if (existing != null) {

            if (existing <= occurrences) {
                this.backingMap.remove(element);
                this.size -= existing;
            } else {
                this.backingMap.put(element, existing - occurrences);
                this.size -= occurrences;
            }

            return true;
        }
        return false;
    }

    public void forEachWithOccurrences(BiConsumer<T, Integer> biConsumer) {
        this.backingMap.forEach(biConsumer);
    }

    public void forEach(Consumer<T> consumer) {
        this.backingMap.forEach((element, count) -> {
            for (int i = 0; i < count; i++) {
                consumer.accept(element);
            }
        });
    }
}
