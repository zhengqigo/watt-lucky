package org.fuelteam.watt.lucky.async;

public abstract class AbstractFunction<T> {

    private Callback<T> callback;

    public AbstractFunction(Callback<T> callback) {
        this.callback = callback;
    }

    public Callback<T> getCallback() {
        return callback;
    }

    public abstract T execute() throws Exception;
}