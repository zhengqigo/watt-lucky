package org.fuelteam.watt.lucky.async;

public interface Callback<T> {

    public void onSuccess(T t);

    public void onFailure(Exception ex);
}