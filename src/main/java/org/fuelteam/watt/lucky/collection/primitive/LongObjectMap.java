package org.fuelteam.watt.lucky.collection.primitive;

import java.util.Map;

public interface LongObjectMap<V> extends Map<Long, V> {

    interface PrimitiveEntry<V> {

        long key();

        V value();

        void setValue(V value);
    }

    V get(long key);

    V put(long key, V value);

    V remove(long key);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(long key);
}