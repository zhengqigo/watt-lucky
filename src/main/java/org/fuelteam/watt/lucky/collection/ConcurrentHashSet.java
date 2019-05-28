package org.fuelteam.watt.lucky.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable {

	private static final long serialVersionUID = -8672117787651310382L;

	private final Map<E, Boolean> map;

	private transient Set<E> set;

	public ConcurrentHashSet() {
		this.map = new ConcurrentHashMap<E, Boolean>();
		this.set = map.keySet();
	}

	public void clear() {
	    this.map.clear();
	}

	public int size() {
		return this.map.size();
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean contains(Object o) {
		return this.map.containsKey(o);
	}

	public boolean remove(Object o) {
		return this.map.remove(o) != null;
	}

	public boolean add(E e) {
		return this.map.put(e, Boolean.TRUE) == null;
	}

	public Iterator<E> iterator() {
		return this.set.iterator();
	}

	public Object[] toArray() {
		return this.set.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return this.set.toArray(a);
	}

	@Override
	public String toString() {
		return this.set.toString();
	}

	public int hashCode() {
		return this.set.hashCode();
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