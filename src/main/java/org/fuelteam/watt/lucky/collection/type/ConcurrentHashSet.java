package org.fuelteam.watt.lucky.collection.type;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JDK并没有提供ConcurrenHashSet，考虑到JDK的HashSet也是基于HashMap实现的，因此ConcurrenHashSet也由ConcurrenHashMap完成。
 * <pre>
 * 虽然也可以通过Collections.newSetFromMap(new ConcurrentHashMap())，但声明一个单独的类型，
 * 阅读代码时能更清晰的知道set的并发友好性，代码来自JDK的SetFromMap，去除JDK8接口。
 */
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