package org.fuelteam.watt.lucky.call;

/**
 * 类 AbstractFunction<T>
 *
 * @author Yin weihong (yinwh@gjmetal.com)
 *         <br>
 *         Copyright (c) 国金金属网
 */
public abstract class AbstractFunction<T> {

    private Callback<T> callback;

    /**
     * 构造 AbstractFunction
     *
     * @param callback {@link Callback<T>}
     */
    public AbstractFunction(Callback<T> callback) {
        this.callback = callback;
    }

    /**
     * 构造 AbstractFunction
     */
    public AbstractFunction() {/* empty */}

    public Callback<T> getCallback() {
        return callback;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    /**
     * abstract方法 execute()
     * @throws Exception
     * @return T
     */
    public abstract T execute() throws Exception;
}