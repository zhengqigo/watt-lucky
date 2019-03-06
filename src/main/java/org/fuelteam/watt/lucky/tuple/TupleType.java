package org.fuelteam.watt.lucky.tuple;

public interface TupleType {

    public int size();

    public Class<?> getNthType(int i);

    public Tuple of(Object... values);

    public class DefaultFactory {
        public static TupleType create(final Class<?>... types) {
            return new DefaultTupleType(types);
        }
        
        public static TupleType create(final Integer size) {
            return new DefaultTupleType(size);
        }
    }
}