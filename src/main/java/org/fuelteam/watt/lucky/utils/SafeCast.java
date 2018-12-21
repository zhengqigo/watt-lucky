package org.fuelteam.watt.lucky.utils;

import java.util.Optional;

public interface SafeCast {

    public static <S> Castable cast(S source) {
        return new Castable() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<T> to(Class<T> target) {
                return target.isInstance(source) ? Optional.of((T) source) : Optional.empty();
            }
        };
    }

    @FunctionalInterface
    public interface Castable {
        public <T> Optional<T> to(Class<T> target);
    }
}