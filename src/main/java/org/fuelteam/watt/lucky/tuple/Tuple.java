package org.fuelteam.watt.lucky.tuple;

public interface Tuple {

    public TupleType getType();

    public int size();

    public <T> T getNthValue(int i);
}