package io.costax.rethinking.designpatterns.delegating;

import java.util.Objects;
import java.util.function.Supplier;


/**
 * Returns a supplier which caches the instance retrieved during the first
 * call to {@code get()} and returns that value on subsequent calls to
 * {@code get()}. See:
 * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
 *
 * <p>The returned supplier is thread-safe. The delegate's {@code get()}
 * method will be invoked at most once. The supplier's serialized form does
 * not contain the cached value, which will be recalculated when {@code get()}
 * is called on the reserialized instance.
 *
 * <p>If {@code delegate} is an instance created by an earlier call to {@code
 * memoize}, it is returned directly.
 */
public class LazyThreadSafe<T> {

    private final Supplier<T> delegate;
    private T instance;

    private boolean initialized = false;

    private LazyThreadSafe(final Supplier<T> delegate) {
        this.delegate = delegate;
    }

    LazyThreadSafe memoize(final Supplier<T> delegate) {
        Objects.requireNonNull(delegate);
        return new LazyThreadSafe(delegate);
    }

    public T evaluate() {

        // A 2-field variant of Double Checked Locking.
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    T t = delegate.get();
                    instance = t;
                    initialized = true;
                    return t;
                }
            }
        }

        // NOTE: As an alternative implementation we can use an java.util.concurrent.atomic.AtomicReference<T>

        return instance;
    }
}
