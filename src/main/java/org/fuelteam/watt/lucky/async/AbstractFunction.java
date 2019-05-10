package org.fuelteam.watt.lucky.async;

/**
 * 抽象函数类，需覆盖execute()方法
 */
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