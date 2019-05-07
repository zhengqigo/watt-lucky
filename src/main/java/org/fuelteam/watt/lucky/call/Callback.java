package org.fuelteam.watt.lucky.call;

/**
 * 接口 Callback
 *
 * @author Yin weihong (yinwh@gjmetal.com)
 *         <br>
 *         Copyright (c) 国金金属网
 */
public interface Callback<T> {

    /**
     * 方法 onSuccess()
     */
    public void onSuccess(T t);

    /**
     * 方法 onFailure()
     */
    public void onFailure(Exception ex);
}