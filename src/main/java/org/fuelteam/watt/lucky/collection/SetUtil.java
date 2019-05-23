package org.fuelteam.watt.lucky.collection;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.fuelteam.watt.lucky.annotation.Nullable;
import org.fuelteam.watt.lucky.collection.type.ConcurrentHashSet;

import com.google.common.collect.Sets;

public class SetUtil {

    /**
     * @see com.google.common.collect.Sets#newHashSet()
     */
    public static <T> HashSet<T> newHashSet() {
        return new HashSet<T>();
    }

    /**
     * @see com.google.common.collect.Sets#newHashSet(Object...)
     */
    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> newHashSet(T... elements) {
        return Sets.newHashSet(elements);
    }

    /**
     * @see com.google.common.collect.Sets#newHashSet(Iterable)
     */
    public static <T> HashSet<T> newHashSet(Iterable<? extends T> elements) {
        return Sets.newHashSet(elements);
    }

    /**
     * @see com.google.common.collect.Sets#newHashSetWithExpectedSize(int)
     */
    public static <T> HashSet<T> newHashSetWithCapacity(int expectedSize) {
        return Sets.newHashSetWithExpectedSize(expectedSize);
    }

    /**
     * @see com.google.common.collect.Sets#newTreeSet()
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Comparable> TreeSet<T> newSortedSet() {
        return new TreeSet<T>();
    }

    /**
     * @see com.google.common.collect.Sets#newTreeSet(Comparator)
     */
    public static <T> TreeSet<T> newSortedSet(@Nullable Comparator<? super T> comparator) {
        return Sets.newTreeSet(comparator);
    }

    public static <T> ConcurrentHashSet<T> newConcurrentHashSet() {
        return new ConcurrentHashSet<T>();
    }

    /**
     * 返回不可修改空Set，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptySet()
     */
    public static final <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 返回不可修改空Set，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptySet()
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> emptySetIfNull(final Set<T> set) {
        return set == null ? (Set<T>) Collections.EMPTY_SET : set;
    }

    /**
     * 返回不可修改的一个元素Set，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#singleton(Object)
     */
    public static final <T> Set<T> singletonSet(T o) {
        return Collections.singleton(o);
    }

    /**
     * 返回不可修改Set，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
        return Collections.unmodifiableSet(s);
    }

    /**
     * 从Map构造Set
     * 
     * @see java.util.Collections#newSetFromMap(Map)
     */
    public static <T> Set<T> newSetFromMap(Map<T, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    /**
     * 返回Set1与Set2的并集只读view，尝试修改会抛UnsupportedOperationException
     */
    public static <E> Set<E> unionView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.union(set1, set2);
    }

    /**
     * 返回Set1与Set2的交集只读view，尝试修改会抛UnsupportedOperationException
     */
    public static <E> Set<E> intersectionView(final Set<E> set1, final Set<?> set2) {
        return Sets.intersection(set1, set2);
    }

    /**
     * 返回Set1与Set2的差集只读view，尝试修改会抛UnsupportedOperationException
     */
    public static <E> Set<E> differenceView(final Set<E> set1, final Set<?> set2) {
        return Sets.difference(set1, set2);
    }

    /**
     * 返回Set1与Set2的补集只读view，尝试修改会抛UnsupportedOperationException
     */
    public static <E> Set<E> disjointView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.symmetricDifference(set1, set2);
    }
}