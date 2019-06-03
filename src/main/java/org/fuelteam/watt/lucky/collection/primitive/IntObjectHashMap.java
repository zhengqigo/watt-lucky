package org.fuelteam.watt.lucky.collection.primitive;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 从Netty4.1.9移植的Key为原子类型的集合类, 数据结构上与HashMap不一样, 空间占用与读写性能俱优
 * <pre>
 * https://github.com/netty/netty/blob/4.1/common/src/main/templates/io/netty/util/collection/KObjectHashMap.template
 * <pre>
 *
 * @param <V> The value type stored in the map
 */
public class IntObjectHashMap<V> implements IntObjectMap<V> {

    // default initial capacity
    public static final int DEFAULT_CAPACITY = 8;

    // default load factor
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;

    // Placeholder for null values, so we can use the actual null to mean available
    private static final Object NULL_VALUE = new Object();

    // The maximum number of elements allowed without allocating more space
    private int maxSize;

    // The load factor for the map
    private final float loadFactor;

    private int[] keys;
    private V[] values;
    private int size;
    private int mask;

    private final Set<Integer> keySet = new KeySet();
    private final Set<Entry<Integer, V>> entrySet = new EntrySet();
    private final Iterable<PrimitiveEntry<V>> entries = new Iterable<PrimitiveEntry<V>>() {
        @Override
        public Iterator<PrimitiveEntry<V>> iterator() {
            return new PrimitiveIterator();
        }
    };

    public IntObjectHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public IntObjectHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public IntObjectHashMap(int initialCapacity, float loadFactor) {
        if (loadFactor <= 0.0f || loadFactor > 1.0f) throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        this.loadFactor = loadFactor;

        // Adjust the initial capacity if necessary
        int capacity = safeFindNextPositivePowerOfTwo(initialCapacity);
        mask = capacity - 1;

        // Allocate the arrays
        keys = new int[capacity];
        @SuppressWarnings({ "unchecked" })
        V[] temp = (V[]) new Object[capacity];
        values = temp;

        // Initialize the maximum size value
        maxSize = calcMaxSize(capacity);
    }

    private static <T> T toExternal(T value) {
        assert value != null : "null is not a legitimate internal value. Concurrent Modification?";
        return value == NULL_VALUE ? null : value;
    }

    @SuppressWarnings("unchecked")
    private static <T> T toInternal(T value) {
        return value == null ? (T) NULL_VALUE : value;
    }

    @Override
    public V get(int key) {
        int index = indexOf(key);
        return index == -1 ? null : toExternal(values[index]);
    }

    @Override
    public V put(int key, V value) {
        int startIndex = hashIndex(key);
        int index = startIndex;

        for (;;) {
            if (values[index] == null) {
                // Found empty slot and use it
                keys[index] = key;
                values[index] = toInternal(value);
                growSize();
                return null;
            }
            if (keys[index] == key) {
                // Found existing entry with this key, just replace the value
                V previousValue = values[index];
                values[index] = toInternal(value);
                return toExternal(previousValue);
            }

            // Can only happen if the map was full at MAX_ARRAY_SIZE and couldn't grow
            if ((index = probeNext(index)) == startIndex) throw new IllegalStateException("Unable to insert");
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends V> sourceMap) {
        if (sourceMap instanceof IntObjectHashMap) {
            // Optimization - iterate through the arrays
            @SuppressWarnings("unchecked")
            IntObjectHashMap<V> source = (IntObjectHashMap<V>) sourceMap;
            for (int i = 0; i < source.values.length; ++i) {
                V sourceValue = source.values[i];
                if (sourceValue != null) put(source.keys[i], sourceValue);
            }
            return;
        }

        // Otherwise just add each entry
        for (Entry<? extends Integer, ? extends V> entry : sourceMap.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(int key) {
        int index = indexOf(key);
        if (index == -1) return null;

        V prev = values[index];
        removeAt(index);
        return toExternal(prev);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(keys, 0);
        Arrays.fill(values, null);
        size = 0;
    }

    @Override
    public boolean containsKey(int key) {
        return indexOf(key) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        @SuppressWarnings("unchecked")
        V v1 = toInternal((V) value);
        for (V v2 : values) {
            if (v2 != null && v2.equals(v1)) return true;
        }
        return false;
    }

    @Override
    public Iterable<PrimitiveEntry<V>> entries() {
        return entries;
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    final PrimitiveIterator iter = new PrimitiveIterator();

                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public V next() {
                        return iter.next().value();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public int hashCode() {
        int hash = size;
        for (int key : keys) {
            hash ^= hashCode(key);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IntObjectMap)) return false;
        @SuppressWarnings("rawtypes")
        IntObjectMap other = (IntObjectMap) obj;
        if (size != other.size()) return false;
        for (int i = 0; i < values.length; ++i) {
            V value = values[i];
            if (value != null) {
                int key = keys[i];
                Object otherValue = other.get(key);
                if (value == NULL_VALUE) {
                    if (otherValue != null) return false;
                } else if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(objectToKey(key));
    }

    @Override
    public V get(Object key) {
        return get(objectToKey(key));
    }

    @Override
    public V put(Integer key, V value) {
        return put(objectToKey(key), value);
    }

    @Override
    public V remove(Object key) {
        return remove(objectToKey(key));
    }

    @Override
    public Set<Integer> keySet() {
        return keySet;
    }

    @Override
    public Set<Entry<Integer, V>> entrySet() {
        return entrySet;
    }

    private int objectToKey(Object key) {
        return ((Integer) key).intValue();
    }

    // Locates the index for the given key, probes using double hashing
    private int indexOf(int key) {
        int startIndex = hashIndex(key);
        int index = startIndex;
        for (;;) {
            if (values[index] == null) return -1;
            if (key == keys[index]) return index;
            if ((index = probeNext(index)) == startIndex) return -1;
        }
    }

    // Returns the hashed index for the given key
    private int hashIndex(int key) {
        // The array lengths are always a power of two, so we can use a bitmask to stay inside the array bounds
        return hashCode(key) & mask;
    }

    // Returns the hash code for the key
    private static int hashCode(int key) {
        return key;
    }

    // Get the next sequential index after {@code index} and wraps if necessary
    private int probeNext(int index) {
        // The array lengths are always a power of two, so we can use a bitmask to stay inside the array bounds
        return (index + 1) & mask;
    }

    // Grows the map size after an insertion, If necessary performs a rehash of the map
    private void growSize() {
        size++;
        if (size > maxSize) {
            if (keys.length == Integer.MAX_VALUE) throw new IllegalStateException("Max capacity reached at size=" + size);
            rehash(keys.length << 1);
        }
    }

    private boolean removeAt(final int index) {
        --size;
        keys[index] = 0;
        values[index] = null;

        int nextFree = index;
        int i = probeNext(index);
        for (V value = values[i]; value != null; value = values[i = probeNext(i)]) {
            int key = keys[i];
            int bucket = hashIndex(key);
            if (i < bucket && (bucket <= nextFree || nextFree <= i) || bucket <= nextFree && nextFree <= i) {
                // Move the displaced entry "back" to the first available position
                keys[nextFree] = key;
                values[nextFree] = value;
                // Put the first entry after the displaced entry
                keys[i] = 0;
                values[i] = null;
                nextFree = i;
            }
        }
        return nextFree != index;
    }

    // Calculates the maximum size allowed before rehashing
    private int calcMaxSize(int capacity) {
        int upperBound = capacity - 1;
        return Math.min(upperBound, (int) (capacity * loadFactor));
    }

    // Rehashes the map for the given capacity
    private void rehash(int newCapacity) {
        int[] oldKeys = keys;
        V[] oldVals = values;

        keys = new int[newCapacity];
        @SuppressWarnings({ "unchecked" })
        V[] temp = (V[]) new Object[newCapacity];
        values = temp;

        maxSize = calcMaxSize(newCapacity);
        mask = newCapacity - 1;

        for (int i = 0; i < oldVals.length; ++i) {
            V oldVal = oldVals[i];
            if (oldVal != null) {
                int oldKey = oldKeys[i];
                int index = hashIndex(oldKey);
                for (;;) {
                    if (values[index] == null) {
                        keys[index] = oldKey;
                        values[index] = oldVal;
                        break;
                    }
                    index = probeNext(index);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder(4 * size);
        sb.append('{');
        boolean first = true;
        for (int i = 0; i < values.length; ++i) {
            V value = values[i];
            if (value != null) {
                if (!first) sb.append(", ");
                sb.append(keyToString(keys[i])).append('=').append(value == this ? "(this Map)" : toExternal(value));
                first = false;
            }
        }
        return sb.append('}').toString();
    }

    protected String keyToString(int key) {
        return Integer.toString(key);
    }

    private final class EntrySet extends AbstractSet<Entry<Integer, V>> {
        @Override
        public Iterator<Entry<Integer, V>> iterator() {
            return new MapIterator();
        }

        @Override
        public int size() {
            return IntObjectHashMap.this.size();
        }
    }

    private final class KeySet extends AbstractSet<Integer> {
        @Override
        public int size() {
            return IntObjectHashMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return IntObjectHashMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return IntObjectHashMap.this.remove(o) != null;
        }

        @Override
        public boolean retainAll(Collection<?> retainedKeys) {
            boolean changed = false;
            for (Iterator<PrimitiveEntry<V>> iter = entries().iterator(); iter.hasNext();) {
                PrimitiveEntry<V> entry = iter.next();
                if (!retainedKeys.contains(entry.key())) {
                    changed = true;
                    iter.remove();
                }
            }
            return changed;
        }

        @Override
        public void clear() {
            IntObjectHashMap.this.clear();
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                private final Iterator<Entry<Integer, V>> iter = entrySet.iterator();

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Integer next() {
                    return iter.next().getKey();
                }

                @Override
                public void remove() {
                    iter.remove();
                }
            };
        }
    }

    private final class PrimitiveIterator implements Iterator<PrimitiveEntry<V>>, PrimitiveEntry<V> {
        private int prevIndex = -1;
        private int nextIndex = -1;
        private int entryIndex = -1;

        private void scanNext() {
            while (++nextIndex != values.length && values[nextIndex] == null) {}
        }

        @Override
        public boolean hasNext() {
            if (nextIndex == -1) scanNext();
            return nextIndex != values.length;
        }

        @Override
        public PrimitiveEntry<V> next() {
            if (!hasNext()) throw new NoSuchElementException();
            prevIndex = nextIndex;
            scanNext();
            entryIndex = prevIndex;
            return this;
        }

        @Override
        public void remove() {
            if (prevIndex == -1) throw new IllegalStateException("next must be called before each remove.");
            if (removeAt(prevIndex)) nextIndex = prevIndex;
            prevIndex = -1;
        }

        @Override
        public int key() {
            return keys[entryIndex];
        }

        @Override
        public V value() {
            return toExternal(values[entryIndex]);
        }

        @Override
        public void setValue(V value) {
            values[entryIndex] = toInternal(value);
        }
    }

    private final class MapIterator implements Iterator<Entry<Integer, V>> {

        private final PrimitiveIterator iter = new PrimitiveIterator();

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Entry<Integer, V> next() {
            if (!hasNext()) throw new NoSuchElementException();
            iter.next();
            return new MapEntry(iter.entryIndex);
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    final class MapEntry implements Entry<Integer, V> {
        private final int entryIndex;

        MapEntry(int entryIndex) {
            this.entryIndex = entryIndex;
        }

        @Override
        public Integer getKey() {
            verifyExists();
            return keys[entryIndex];
        }

        @Override
        public V getValue() {
            verifyExists();
            return toExternal(values[entryIndex]);
        }

        @Override
        public V setValue(V value) {
            verifyExists();
            V prevValue = toExternal(values[entryIndex]);
            values[entryIndex] = toInternal(value);
            return prevValue;
        }

        private void verifyExists() {
            if (values[entryIndex] == null) throw new IllegalStateException("The map entry has been removed");
        }
    }

    public static int safeFindNextPositivePowerOfTwo(final int value) {
        return value <= 0 ? 1 : value >= 0x40000000 ? 0x40000000 : findNextPositivePowerOfTwo(value);
    }

    public static int findNextPositivePowerOfTwo(final int value) {
        assert value > Integer.MIN_VALUE && value < 0x40000000;
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }
}