package io.costax.chapter5;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;

/**
 * <pre>
 *
 * Wildcard types provide additional API flexibility
 *
 *   - List<String> is a subtype of List<? extends Object>
 *   - List<Object> is a subtype of List<? super String>
 *
 * A Mnemonic for wildcards Usage
 *
 * PECS-- **P**roduce extends, **C**onsumer super
 *
 *   - For a T producer, use Foo<? extends T>
 *   - For a T consumer, use Foo<? super T>
 *
 *   Means that:
 *     - when we passing in a parameter from which we want to **produce** **T**, we want to get **T's** from this thing. Then we should use the type **Foo<? extends T>**
 *     - If we passing in a parameter into witch we want to put T's that is the thing that consumes elements of type T. then we should use Foo<? super T>
 *
 * - Only applies wildcars to input parameters
 *    - don't use wildcards types as return types
 * </pre>
 */
public class WildcardsTests {

    @Test(expected = java.lang.ArrayStoreException.class)
    public void arrayCovariant() {
        Object[] values = new String[4];
        values[0] = 4;
    }

    /**
     * Produces Extends Consumer super
     */
    @Test
    public void flexPECSTest() {

        // suppose we want to add bulk methods to Stack<T>
        // void pushAll(Collection<T> src) -> src is a producing, so void pushAll(Collection<? extends T> src)
        // void popAll(Collection<T> dst) -> dst is a consumer, so void popAll(Collection<? super T> dst)

        WildStack<Number> stack = new WildStack<>();

        final List<Integer> integers = Arrays.asList(1, 2);
        stack.pushAll(integers);

        final List<Double> doubles = Arrays.asList(Double.valueOf("1.9"), Double.valueOf("13.6"), Double.valueOf("7.4"));
        stack.pushAll(doubles);

        final Set<Number> values = stack.getValues();
        Assert.assertThat(values.size(), is(5));

        // Now get The All values
        final Collection<? super Number> destinantion = new ArrayList<>();
        stack.popAll(destinantion);

        Assert.assertThat(destinantion.size(), is(5));


        // using usion of two diferents types

        WildStack<Integer> stackOfInteger = new WildStack<>();
        stackOfInteger.pushAll(Arrays.asList(1, 2, 3));

        WildStack<Double> stackOdDoubles = new WildStack<>();
        final List<Double> src = Collections.singletonList(5.5d);
        stackOdDoubles.pushAll(src);

        //final WildStack<? extends Number> union = WildStack.union(stackOfInteger, stackOdDoubles);
        //final WildStack<Number> union = WildStack.union(stackOfInteger, stackOdDoubles);
        final WildStack<Number> union = WildStack.union(stackOfInteger, stackOdDoubles);

        Assert.assertThat(union.size(), is(4));
    }

    public static class WildStack<T> {

        private Set<T> values = new HashSet<>();

        public static <T> WildStack<T> union(WildStack<? extends T> s1, WildStack<? extends T> s2) {
            // we should never use wildcard in the return type
            final WildStack<T> objects = new WildStack<>();
            objects.pushAll(s1.getValues());
            objects.pushAll(s2.getValues());

            return objects;
        }

        void pushAll(Collection<? extends T> src) {
            this.values.addAll(src);
        }

        void popAll(Collection<? super T> dst) {
            dst.addAll(this.values);
            this.values.removeAll(dst);
        }

        Set<T> getValues() {
            final Set<T> ts1 = new HashSet<>(this.values);
            return Collections.unmodifiableSet(ts1);
            //final ImmutableList<T> ts = ImmutableList.copyOf(this.values);
        }

        int size() {
            return this.values.size();
        }
    }
}
