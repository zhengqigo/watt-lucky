package org.fuelteam.watt.lucky.parallel;

public interface IParallel<T> {

    public void execute(T context);
}