package io.costax.diy.collections;

import java.util.ArrayList;
import java.util.Collection;

public class ListMultimap<K, V> extends Multimap<K, V> {
    @Override
    protected Collection<V> getEmptyBackingCollection() {
        return new ArrayList<>();
    }
}
