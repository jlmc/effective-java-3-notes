package io.costax.chapter11;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Item 81: Prefer concurrency utilities to wait and
 * notify
 */
public class UsingConcurrentHashMap {

    private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

    @Test
    public void name() {




    }

    /**
     * Because you can’t exclude concurrent activity on concurrent collections, you
     * can’t atomically compose method invocations on them either. Therefore,
     * concurrent collection interfaces were outfitted with state-dependent modify
     * operations, which combine several primitives into a single atomic operation.
     * These operations proved sufficiently useful on concurrent collections that they
     * were added to the corresponding collection interfaces in Java 8, using default
     * methods (Item 21).
     * For example, Map’s putIfAbsent(key, value) method inserts a
     * mapping for a key if none was present and returns the previous value associated
     * with the key, or null if there was none. This makes it easy to implement
     * thread-safe canonicalizing maps. This method simulates the behavior of
     * String.intern:
     */
    @Deprecated
    public static String intern1(String s) {
        String previousValue = map.putIfAbsent(s, s);
        return previousValue == null ? s : previousValue;
    }

    /**
     * In fact, you can do even better. ConcurrentHashMap is optimized for
     * retrieval operations, such as get.
     * Therefore, it is worth invoking get initiallyand calling putIfAbsent
     * only if get indicates that it is necessary.
     */

    // Concurrent canonicalizing map atop ConcurrentMap - faster!
    public static String intern(String s) {
        String result = map.get(s);
        if (result == null) {
            result = map.putIfAbsent(s, s);
            if (result == null)
                result = s;
        }
        return result;
    }
}
