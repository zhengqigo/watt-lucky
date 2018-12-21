package org.fuelteam.watt.lucky.call;

public abstract class AbstractFunction<T> {

    private Callback<T> callback;

    public AbstractFunction(Callback<T> callback) {
        this.callback = callback;
    }

    public AbstractFunction() {/* nothing */}

    public Callback<T> getCallback() {
        return callback;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public abstract T execute() throws Exception;
}
