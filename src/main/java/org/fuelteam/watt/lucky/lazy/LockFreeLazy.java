package org.fuelteam.watt.lucky.lazy;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LockFreeLazy<T> implements Lazy<T> {

    private static final Object INITIALIZING = new Object();

    private static final Object ERROR = new Object();

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<LockFreeLazy, Object> UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(LockFreeLazy.class, Object.class, "instance");

    private final Supplier<T> supplier;

    private volatile Object instance;

    public LockFreeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        final Object value = getValue();
        checkError(value);
        if (isValueSet(value)) return (T) value;
        return initializeAndGet();
    }

    @SuppressWarnings("unchecked")
    private T initializeAndGet() {
        if (trySetValue()) setValue();
        final Object value = waitForValue();
        checkError(value);
        return (T) value;
    }

    private boolean isValueSet(Object value) {
        return Objects.nonNull(value) && value != INITIALIZING && value != ERROR;
    }

    private boolean trySetValue() {
        return UPDATER.compareAndSet(this, null, INITIALIZING);
    }

    private void setValue() {
        try {
            T newValue = supplier.get();
            if (Objects.isNull(newValue)) throw new IllegalStateException("Supplier failed to provide the value");
            UPDATER.set(this, newValue);
        } catch (Exception ex) {
            UPDATER.set(this, ERROR);
            throw new IllegalStateException("Failed to initialize the lazy instance", ex);
        }
    }

    private Object getValue() {
        return instance;
    }

    private Object waitForValue() {
        Object value;
        do {
            value = getValue();
        } while (value == INITIALIZING);
        return value;
    }

    private void checkError(Object value) {
        if (value == ERROR) throw new IllegalStateException("Supplier failed to initialize the value");
    }
}