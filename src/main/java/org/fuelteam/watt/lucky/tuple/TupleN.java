package org.fuelteam.watt.lucky.tuple;

import java.io.Serializable;
import java.util.Arrays;

public class TupleN implements Serializable {

    private static final long serialVersionUID = -7139903442004737461L;

    private Class<?>[] clazz;

    private Integer size;

    private Object[] values;

    public TupleN(Class<?>... clazz) {
        this.clazz = (clazz != null ? clazz : new Class<?>[0]);
        this.size = this.clazz.length;
        this.values = new Object[size];
        System.arraycopy(values, 0, this.values, 0, size);
    }

    public TupleN(final Integer size) {
        this.clazz = new Class<?>[size];
        this.size = size;
        this.values = new Object[size];
        System.arraycopy(values, 0, this.values, 0, size);
    }

    public Integer size() {
        return this.size;
    }

    public void clear() {
        this.clazz = null;
        this.size = 0;
        this.values = null;
    }

    public Class<?> getNthType(int i) {
        return clazz[i];
    }

    public void setNthValue(int i, Object value) {
        values[i] = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getNthValue(int i) {
        return (T) values[i];
    }

    public TupleN of(Object... values) {
        final boolean check1 = (values == null && clazz.length == 0);
        final boolean check2 = (values != null && values.length != clazz.length);
        if (check1 || check2) {
            Object butValue = values == null ? "(null)" : values.length;
            String mesasge = String.format("Expected %s values but %s", clazz.length, butValue);
            throw new IllegalArgumentException(mesasge);
        }

        if (values != null) {
            for (int i = 0; i < clazz.length; i++) {
                final Class<?> nthType = clazz[i] == null ? Object.class : clazz[i];
                final Object nthValue = values[i];
                if (nthValue != null && !nthType.isAssignableFrom(nthValue.getClass())) {
                    String template = "Expected value #%s ('%s') of new Tuple to be %s but %s";
                    String message = String.format(template, i, nthValue, nthType, nthValue.getClass());
                    throw new IllegalArgumentException(message);
                }
            }
        }

        if (values == null || values.length == 0) {
            this.values = new Object[0];
        } else {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;

        if (!(object instanceof TupleN)) return false;

        final TupleN other = (TupleN) object;
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