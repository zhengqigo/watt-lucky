package org.fuelteam.watt.lucky.collection.type;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable {

	private static final long serialVersionUID = -8672117787651310382L;

	private final Map<E, Boolean> map;

	private transient Set<E> set; // Its keySet

	public ConcurrentHashSet() {
		map = new ConcurrentHashMap<E, Boolean>();
		set = map.keySet();
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	public boolean remove(Object o) {
		return map.remove(o) != null;
	}

	public boolean add(E e) {
		return map.put(e, Boolean.TRUE) == null;
	}

	public Iterator<E> iterator() {
		return set.iterator();
	}

	public Object[] toArray() {
		return set.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}

	@Override
	public String toString() {
		return set.toString();
	}

	public int hashCode() {
		return set.hashCode();
	}

	public boolean equals(Object o) {
		return o == this || set.equals(o);
	}

	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return set.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return set.retainAll(c);
	}
}