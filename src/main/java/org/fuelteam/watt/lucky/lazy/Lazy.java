package org.fuelteam.watt.lucky.lazy;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Lazy<T> {

    public T get();

    public default <U> Lazy<U> map(Function<T, U> map) {
        return create(() -> map.apply(get()));
    }

    public default <U> Lazy<U> flatMap(Function<T, Lazy<U>> map) {
        return create(() -> map.apply(get()).get());
    }

    public static <T> Lazy<T> create(Supplier<T> supplier) {
        return new LockFreeLazy<>(supplier);
    }
}