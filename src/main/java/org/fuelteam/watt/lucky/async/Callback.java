package org.fuelteam.watt.lucky.async;

/**
 * 回调接口
 */
public interface Callback<T> {

    public void onSuccess(T t);

    public void onFailure(Exception ex);
}