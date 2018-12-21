package org.fuelteam.watt.lucky.utils;

import java.util.Optional;

public interface Condition {

    public static Conditional when(boolean condition) {
        return new Conditional() {
            @Override
            public <T> Optional<T> then(T value) {
                return condition ? Optional.of(value) : Optional.empty();
            }
        };
    }

    public interface Conditional {
        public <T> Optional<T> then(T value);
    }
}