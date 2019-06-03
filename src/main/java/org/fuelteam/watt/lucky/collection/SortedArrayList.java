package org.fuelteam.watt.lucky.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 从Jodd复制, 部分和index相关操作不支持, 如 add(index, element), 改进Comparator泛型定义, 改进findInsertionPoint的位移
 * <pre>
 * https://github.com/oblac/jodd/blob/master/jodd-core/src/main/java/jodd/util/collection/SortedArrayList.java
 * <pre>
 */
public final class SortedArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -8301136559614447593L;

    protected final Comparator<? super E> comparator;

    public SortedArrayList(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public SortedArrayList() {
        this.comparator = null;
    }

    public SortedArrayList(Collection<? extends E> collection) {
        this.comparator = null;
        addAll(collection);
    }

    public Comparator<?> getComparator() {
        return comparator;
    }

    @Override
    public boolean add(E e) {
        int idx = 0;
        if (!isEmpty()) idx = findInsertionPoint(e);
        super.add(idx, e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        boolean changed = false;
        while (it.hasNext()) {
            boolean ret = add(it.next());
            if (!changed) changed = ret;
        }
        return changed;
    }

    public int findInsertionPoint(E e) {
        return findInsertionPoint(e, 0, size() - 1);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected int compare(E e1, E e2) {
        if (this.comparator == null) return ((Comparable) e1).compareTo(e2);
        return this.comparator.compare(e1, e2);
    }

    protected int findInsertionPoint(E o, int originalLow, int originalHigh) {
        int low = originalLow;
        int high = originalHigh;
        while (low <= high) {
            int mid = low + ((high - low) >>> 1);
            int delta = compare(get(mid), o);
            if (delta > 0) high = mid - 1;
            if (delta <= 0) low = mid + 1;
        }
        return low;
    }
}