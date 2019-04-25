package org.fuelteam.watt.lucky.tuple;

public interface Tuple {

    public TupleType getType();

    public int size();
    
    public void clear();

    public <T> T getNthValue(int i);
}