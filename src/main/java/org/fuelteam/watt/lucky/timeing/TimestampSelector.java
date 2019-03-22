package org.fuelteam.watt.lucky.timeing;

/**
 * The TimestampSelector is used to pull a timestamp from an object.
 */
public interface TimestampSelector<T> {
    long select(T t);
}