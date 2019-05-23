package org.fuelteam.watt.lucky.collection.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 从Jodd复制，部分和index相关操作不支持，如 add(index, element)，改进Comparator泛型定义，改进findInsertionPoint的位移
 * <pre>
 * https://github.com/oblac/jodd/blob/master/jodd-core/src/main/java/jodd/util/collection/SortedArrayList.java
 * <pre>
 */
public final class SortedArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -8301136559614447593L;

	protected final Comparator<? super E> comparator;

	public SortedArrayList(Comparator<? super E> c) {
		comparator = c;
	}

	public SortedArrayList() {
		comparator = null;
	}

	public SortedArrayList(Collection<? extends E> c) {
		comparator = null;
		addAll(c);
	}

	public Comparator<?> getComparator() {
		return comparator;
	}

	@Override
	public boolean add(E o) {
		int idx = 0;
		if (!isEmpty()) idx = findInsertionPoint(o);
		super.add(idx, o);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Iterator<? extends E> i = c.iterator();
		boolean changed = false;
		while (i.hasNext()) {
			boolean ret = add(i.next());
			if (!changed) changed = ret;
		}
		return changed;
	}

	public int findInsertionPoint(E o) {
		return findInsertionPoint(o, 0, size() - 1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int compare(E k1, E k2) {
		if (comparator == null) return ((Comparable) k1).compareTo(k2);
		return comparator.compare(k1, k2);
	}

	protected int findInsertionPoint(E o, int originalLow, int originalHigh) {
		int low = originalLow;
		int high = originalHigh;
		while (low <= high) {
			int mid = low + ((high - low) >>> 1);
			int delta = compare(get(mid), o);
			if (delta > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return low;
	}
}