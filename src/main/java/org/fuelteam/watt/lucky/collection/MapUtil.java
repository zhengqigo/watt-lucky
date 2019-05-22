package org.fuelteam.watt.lucky.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.commons.lang3.Validate;
import org.fuelteam.watt.lucky.annotation.NotNull;
import org.fuelteam.watt.lucky.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

public class MapUtil {

    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return (map != null) && !map.isEmpty();
    }

    /**
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#putIfAbsent(ConcurrentMap, Object, Object)
     */
    public static <K, V> V putIfAbsentReturnLast(@NotNull final ConcurrentMap<K, V> map, final K key, final V value) {
        final V result = map.putIfAbsent(key, value);
        return result != null ? result : value;
    }

    /**
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#createIfAbsent(ConcurrentMap, Object,
     * org.apache.commons.lang3.concurrent.ConcurrentInitializer)
     */
    public static <K, V> V createIfAbsentReturnLast(@NotNull final ConcurrentMap<K, V> map, final K key,
            @NotNull final ValueCreator<? extends V> creator) {
        final V value = map.get(key);
        if (value == null) return putIfAbsentReturnLast(map, key, creator.get());
        return value;
    }

    /**
     * @see MapUtil#createIfAbsentReturnLast(ConcurrentMap, Object, ValueCreator)
     */
    public interface ValueCreator<T> {
        T get();
    }

    /**
     * @see com.google.common.collect.Maps#newHashMap(int)
     */
    public static <K, V> HashMap<K, V> newHashMapWithCapacity(int expectedSize, float loadFactor) {
        int finalSize = (int) (expectedSize / loadFactor + 1.0F);
        return new HashMap<K, V>(finalSize, loadFactor);
    }

    public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap(@NotNull final K[] keys, @NotNull final V[] values) {
        Validate.isTrue(keys.length == values.length, "keys.length is %d, but values.length is %d", keys.length, values.length);
        HashMap<K, V> map = new HashMap<K, V>(keys.length * 2);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap(@NotNull final List<K> keys, @NotNull final List<V> values) {
        Validate.isTrue(keys.size() == values.size(), "keys.length is %s, but values.length is %s", keys.size(), values.size());
        HashMap<K, V> map = new HashMap<K, V>(keys.size() * 2);
        Iterator<K> keyIt = keys.iterator();
        Iterator<V> valueIt = values.iterator();
        while (keyIt.hasNext()) {
            map.put(keyIt.next(), valueIt.next());
        }
        return map;
    }

    /**
     * @see com.google.common.collect.Maps#newTreeMap()
     */
    @SuppressWarnings("rawtypes")
    public static <K extends Comparable, V> TreeMap<K, V> newSortedMap() {
        return new TreeMap<K, V>();
    }

    /**
     * @see com.google.common.collect.Maps#newTreeMap(Comparator)
     */
    public static <C, K extends C, V> TreeMap<K, V> newSortedMap(@Nullable Comparator<C> comparator) {
        return Maps.newTreeMap(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(@NotNull Class<K> type) {
        return new EnumMap<K, V>(Preconditions.checkNotNull(type));
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSortedMap() {
        return new ConcurrentSkipListMap<K, V>();
    }

    /**
     * 返回空Map，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptyMap()
     */
    public static final <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    /**
     * 返回不可修改的Map，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptyMap()
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> emptyMapIfNull(final Map<K, V> map) {
        return map == null ? (Map<K, V>) Collections.EMPTY_MAP : map;
    }

    /**
     * 返回不可修改的Map，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#singletonMap(Object, Object)
     */
    public static <K, V> Map<K, V> singletonMap(final K key, final V value) {
        return Collections.singletonMap(key, value);
    }

    /**
     * 返回不可修改的Map，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public static <K, V> Map<K, V> unmodifiableMap(final Map<? extends K, ? extends V> m) {
        return Collections.unmodifiableMap(m);
    }

    /**
     * 返回不可修改的有序Map
     * 
     * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
     */
    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(final SortedMap<K, ? extends V> m) {
        return Collections.unmodifiableSortedMap(m);
    }

    /**
     * 对两个Map进行比较，包括key的差集，key的交集，以及key相同但value不同的元素
     * 
     * @see com.google.common.collect.MapDifference
     */
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        return Maps.difference(left, right);
    }

    /**
     * 对Map按Value进行排序，返回排序LinkedHashMap
     * 
     * @param reverse 按Value的倒序 or 正序排列
     */
    @SuppressWarnings("rawtypes")
    public static <K, V extends Comparable> Map<K, V> sortByValue(Map<K, V> map, final boolean reverse) {
        return sortByValueInternal(map, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<K, V>());
    }

    /**
     * 对Map按Value进行排序，返回排序LinkedHashMap
     */
    public static <K, V> Map<K, V> sortByValue(Map<K, V> map, final Comparator<? super V> comparator) {
        return sortByValueInternal(map, new EntryValueComparator<K, V>(comparator));
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> sortByValueInternal(Map<K, V> map, Comparator<Entry<K, V>> comparator) {
        Set<Entry<K, V>> entrySet = map.entrySet();
        Entry<K, V>[] entryArray = entrySet.toArray(new Entry[0]);

        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Entry<K, V> entry : entryArray) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 对Map按Value进行排序，返回排序LinkedHashMap，最多返回n条
     * @param reverse 按Value的倒序 or 正序排列
     */
    @SuppressWarnings("rawtypes")
    public static <K, V extends Comparable> Map<K, V> topNByValue(Map<K, V> map, final boolean reverse, int n) {
        return topNByValueInternal(map, n, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<K, V>());
    }

    /**
     * 对Map按Value进行排序，返回排序LinkedHashMap，最多返回n条
     */
    public static <K, V> Map<K, V> topNByValue(Map<K, V> map, final Comparator<? super V> comparator, int n) {
        return topNByValueInternal(map, n, new EntryValueComparator<K, V>(comparator));
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> topNByValueInternal(Map<K, V> map, int n, Comparator<Entry<K, V>> comparator) {
        Set<Entry<K, V>> entrySet = map.entrySet();
        Entry<K, V>[] entryArray = entrySet.toArray(new Entry[0]);
        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<K, V>();
        int size = Math.min(n, entryArray.length);
        for (int i = 0; i < size; i++) {
            Entry<K, V> entry = entryArray[i];
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private static final class ComparableEntryValueComparator<K, V extends Comparable> implements Comparator<Entry<K, V>> {
        @SuppressWarnings("unchecked")
        @Override
        public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return (o1.getValue()).compareTo(o2.getValue());
        }
    }

    private static final class EntryValueComparator<K, V> implements Comparator<Entry<K, V>> {
        private final Comparator<? super V> comparator;

        private EntryValueComparator(Comparator<? super V> comparator2) {
            this.comparator = comparator2;
        }

        @Override
        public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return comparator.compare(o1.getValue(), o2.getValue());
        }
    }
}