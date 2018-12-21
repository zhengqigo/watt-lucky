package org.fuelteam.watt.lucky.call;

public interface Callback<T> {

    public void onSuccess(T t);

    public void onFailure(Exception ex);
}
