package org.fuelteam.watt.lucky.async;

import org.fuelteam.watt.lucky.annotation.NotNull;

public abstract class AbstractFunction<T> {

    private Callback<T> callback;

    public AbstractFunction(@NotNull Callback<T> callback) {
        this.callback = callback;
    }

    public Callback<T> getCallback() {
        return callback;
    }

    public abstract T execute() throws Exception;
}