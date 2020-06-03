package io.costax.diy.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Multimap (MultiValueMap)
 * <p>
 * A tradicional Map has a key to value relationship, but it is single key to single value.
 * We want to have a relationship, of single key and multiple values
 * Multiple values = Another collection can be List, Set or Bag.
 *
 * @see <a href="https://www.eclipse.org/collections/">Eclipse Collections</a>
 * @see <a href="https://github.com/eclipse/eclipse-collections">eclipse collections github</a>
 */
public abstract class Multimap<K, V> {

    protected Map<K, Collection<V>> backingMap = new HashMap<>();

    protected abstract Collection<V> getEmptyBackingCollection();

    public Collection<V> get(K key) {
        return backingMap.getOrDefault(key, getEmptyBackingCollection());
    }

    public boolean put(final K key, final V value) {
        Collection<V> values = backingMap.getOrDefault(key, getEmptyBackingCollection());
        boolean added = values.add(value);
        backingMap.put(key, values);
        return added;
    }

    public Collection<V> putAll(K key, Iterable<V> values) {
        Collection<V> existingValue = this.backingMap
                .getOrDefault(key, this.getEmptyBackingCollection());
        for (V value : values) {
            existingValue.add(value);
        }

        return existingValue;
    }

    public Collection<V> removeAll(K key) {
        return this.backingMap.remove(key);
    }

    public boolean remove(K key, V value) {
        Collection<V> existing = this.backingMap.getOrDefault(key, this.getEmptyBackingCollection());
        if (!existing.isEmpty()) {
            boolean removed = existing.remove(value);
            if (removed && existing.isEmpty()) {
                this.backingMap.remove(key);
            }
            return removed;
        }
        return false;
    }
}
