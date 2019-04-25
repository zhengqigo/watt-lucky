package org.fuelteam.watt.lucky.tuple;

import java.io.Serializable;

public class DefaultTupleType implements TupleType, Serializable {

    private static final long serialVersionUID = -7139903442004737461L;

    Class<?>[] types;

    Integer size;

    public DefaultTupleType(Class<?>[] types) {
        this.types = (types != null ? types : new Class<?>[0]);
        this.size = this.types.length;
    }

    public DefaultTupleType(final Integer size) {
        this.types = new Class<?>[size];
        this.size = size;
    }

    public int size() {
        return types.length;
    }
    
    public void clear() {
        this.types = null;
        this.size = 0;
    }

    public Class<?> getNthType(int i) {
        return types[i];
    }

    public Tuple of(Object... values) {
        final boolean check1 = (values == null && types.length == 0);
        final boolean check2 = (values != null && values.length != types.length);
        if (check1 || check2) {
            Object butValue = values == null ? "(null)" : values.length;
            String mesasge = String.format("Expected %s values but %s", types.length, butValue);
            throw new IllegalArgumentException(mesasge);
        }

        if (values != null) {
            for (int i = 0; i < types.length; i++) {
                final Class<?> nthType = types[i] == null ? Object.class : types[i];
                final Object nthValue = values[i];
                if (nthValue != null && !nthType.isAssignableFrom(nthValue.getClass())) {
                    String template = "Expected value #%s ('%s') of new Tuple to be %s but %s";
                    String message = String.format(template, i, nthValue, nthType, nthValue.getClass());
                    throw new IllegalArgumentException(message);
                }
            }
        }
        return new DefaultTuple(this, values);
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public Integer getSize() {
        return size;
    }
}