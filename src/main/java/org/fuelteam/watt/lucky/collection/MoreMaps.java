package org.fuelteam.watt.lucky.collection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.fuelteam.watt.lucky.primitive.IntObjectHashMap;
import org.fuelteam.watt.lucky.primitive.LongObjectHashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeRangeMap;
import com.google.common.util.concurrent.AtomicLongMap;

public class MoreMaps {

    public static <K, V> ConcurrentMap<K, V> createWeakKeyConcurrentMap(int initialCapacity, int concurrencyLevel) {
        return new MapMaker().weakKeys().initialCapacity(initialCapacity).concurrencyLevel(concurrencyLevel).makeMap();
    }

    public static <K, V> ConcurrentMap<K, V> createWeakValueConcurrentMap(int initialCapacity, int concurrencyLevel) {
        return new MapMaker().weakValues().initialCapacity(initialCapacity).concurrencyLevel(concurrencyLevel).makeMap();
    }

    public static <V> IntObjectHashMap<V> createPrimitiveIntKeyMap(int initialCapacity, float loadFactor) {
        return new IntObjectHashMap<V>(initialCapacity, loadFactor);
    }

    public static <V> LongObjectHashMap<V> createPrimitiveLongKeyMap(int initialCapacity, float loadFactor) {
        return new LongObjectHashMap<V>(initialCapacity, loadFactor);
    }

    public static <K> HashMap<K, MutableInt> createMutableIntValueMap(int initialCapacity, float loadFactor) {
        return new HashMap<K, MutableInt>(initialCapacity, loadFactor);
    }

    public static <K> HashMap<K, MutableLong> createMutableLongValueMap(int initialCapacity, float loadFactor) {
        return new HashMap<K, MutableLong>(initialCapacity, loadFactor);
    }

    public static <E> AtomicLongMap<E> createConcurrentCounterMap() {
        return AtomicLongMap.create();
    }

    public static <K, V> ArrayListMultimap<K, V> createListMultiValueMap(int expectedKeys, int expectedValuesPerKey) {
        return ArrayListMultimap.create(expectedKeys, expectedValuesPerKey);
    }

    @SuppressWarnings("rawtypes")
    public static <K, V extends Comparable> SortedSetMultimap<K, V> createSortedSetMultiValueMap() {
        return MultimapBuilder.hashKeys().treeSetValues().build();
    }

    @SuppressWarnings("unchecked")
    public static <K, V> SortedSetMultimap<K, V> createSortedSetMultiValueMap(Comparator<V> comparator) {
        return (SortedSetMultimap<K, V>) MultimapBuilder.hashKeys().treeSetValues(comparator);
    }

    @SuppressWarnings("rawtypes")
    public static <K extends Comparable, V> TreeRangeMap<K, V> createRangeMap() {
        return TreeRangeMap.create();
    }
}