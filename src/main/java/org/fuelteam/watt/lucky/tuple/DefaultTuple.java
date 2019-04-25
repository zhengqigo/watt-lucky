package org.fuelteam.watt.lucky.tuple;

import java.io.Serializable;
import java.util.Arrays;

public class DefaultTuple implements Tuple, Serializable {

    private static final long serialVersionUID = 4770198448986401164L;

    private TupleType type;

    private Object[] values;

    public DefaultTuple(TupleType type, Object[] values) {
        this.type = type;
        if (values == null || values.length == 0) {
            this.values = new Object[0];
        } else {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
    }

    @Override
    public TupleType getType() {
        return type;
    }
    
    @Override
    public void clear() {
        this.values = null;
        this.type = null;
    }

    @Override
    public int size() {
        return values.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getNthValue(int i) {
        return (T) values[i];
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;

        if (!(object instanceof Tuple)) return false;

        final Tuple other = (Tuple) object;
        final int size = size();
        if (other.size() != size) return false;
        for (int i = 0; i < size; i++) {
            final Object thisNthValue = getNthValue(i);
            final Object otherNthValue = other.getNthValue(i);
            
            final boolean check1 = (thisNthValue == null && otherNthValue != null);
            final boolean check2 = (thisNthValue != null && !thisNthValue.equals(otherNthValue));
            if (check1 || check2) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (Object value : values) {
            if (value != null) hash = hash * 37 + value.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}