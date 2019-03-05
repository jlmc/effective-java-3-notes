package io.costax.chapter5;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TypesafeHeterogeneousContainerPatternTest {

    @Test
    public void thc_pattern_tests() {
        Favorites f = new Favorites();

        f.putFavorite(String.class, "java");
        f.putFavorite(Integer.class, 123);
        f.putFavorite(Class.class, ThreadLocal.class);

        System.out.println("--");

        final String fString = f.getFavorite(String.class);
        final Integer fInteger = f.getFavorite(Integer.class);
        final Class favoriteClass = f.getFavorite(Class.class);

        System.out.printf("%s %x %s\n", fString, fInteger, favoriteClass);
        Assert.assertEquals("java", fString);
    }

    private static class Favorites {

        private Map<Class<?>, Object> cache = new HashMap<>();

        <T> void putFavorite(Class<T> type, T instance) {
            if (type == null)
                throw new NullPointerException("Type is null");
            cache.put(type, instance);
        }

        <T> T getFavorite(Class<T> type) {
            //return (T) cache.get(type);
            return type.cast(cache.get(type));
        }
    }
}
