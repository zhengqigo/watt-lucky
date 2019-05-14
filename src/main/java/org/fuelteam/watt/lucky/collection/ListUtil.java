package org.fuelteam.watt.lucky.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.Lists;

public class ListUtil {

    public static boolean isEmpty(List<?> list) {
        return (list == null) || list.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return (list != null) && !(list.isEmpty());
    }

    /**
     * 获取第一个元素，如果List为空返回null
     */
    public static <T> T getFirst(List<T> list) {
        if (isEmpty(list)) return null;
        return list.get(0);
    }

    /**
     * 获取最后一个元素，如果List为空返回null
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) return null;
        return list.get(list.size() - 1);
    }

    /**
     * @see com.google.common.collect.Lists#newArrayList(Object...)
     */
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> newArrayList(T... elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * @see com.google.common.collect.Lists#newArrayList(Iterable)
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * @see com.google.common.collect.Lists#newArrayListWithCapacity(int)
     */
    public static <T> ArrayList<T> newArrayListWithCapacity(int initSize) {
        return new ArrayList<T>(initSize);
    }

    /**
     * @see com.google.common.collect.Lists#newLinkedList()
     */
    public static <T> LinkedList<T> newLinkedList(Iterable<? extends T> elements) {
        return Lists.newLinkedList(elements);
    }

    @SuppressWarnings("unchecked")
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(T... elements) {
        return new CopyOnWriteArrayList<T>(elements);
    }

    /**
     * 返回不可修改空List，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptyList()
     */
    public static final <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 返回不可修改List，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#emptyList()
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> emptyListIfNull(final List<T> list) {
        return list == null ? (List<T>) Collections.EMPTY_LIST : list;
    }

    /**
     * 返回不可修改List，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#singletonList(Object)
     */
    public static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    /**
     * 返回不可修改List，尝试修改会抛UnsupportedOperationException
     * 
     * @see java.util.Collections#unmodifiableList(List)
     */
    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * 返回同步的List，所有方法都会被synchronized原语同步，尝试修改会抛UnsupportedOperationException
     */
    public static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    /**
     * 升序，使用元素自身的compareTo()方法
     * 
     * @see java.util.Collections#sort(List)
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        Collections.sort(list);
    }

    /**
     * 倒序，使用元素自身的compareTo()方法
     * 
     * @see java.util.Collections#sort(List)
     */
    public static <T extends Comparable<? super T>> void sortReverse(List<T> list) {
        Collections.sort(list, Collections.reverseOrder());
    }

    /**
     * 升序，使用Comparetor
     * 
     * @see java.util.Collections#sort(List, Comparator)
     */
    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, c);
    }

    /**
     * 倒序，使用Comparator
     * 
     * @see java.util.Collections#sort(List, Comparator)
     */
    public static <T> void sortReverse(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, Collections.reverseOrder(c));
    }

    /**
     * 二分法快速查找Comparable对象，list必须升序，不存在则返回一个负数，代表要插入对象的应该位置
     * 
     * @see java.util.Collections#binarySearch(List, Object)
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> sortedList, T key) {
        return Collections.binarySearch(sortedList, key);
    }

    /**
     * 二分法快速查找Comparator对象，list必须升序，不存在则返回一个负数，代表要插入对象的应该位置
     * 
     * @see java.util.Collections#binarySearch(List, Object, Comparator)
     */
    public static <T> int binarySearch(List<? extends T> sortedList, T key, Comparator<? super T> c) {
        return Collections.binarySearch(sortedList, key, c);
    }

    /**
     * 使用默认的Random随机乱序
     * 
     * @see java.util.Collections#shuffle(List)
     */
    public static void shuffle(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * 使用传入的Random随机乱序
     * 
     * @see java.util.Collections#shuffle(List, Random)
     */
    public static void shuffle(List<?> list, Random rnd) {
        Collections.shuffle(list, rnd);
    }

    /**
     * 返回倒转顺序访问的List的View，不会实际多生成
     * 
     * @see com.google.common.collect.Lists#reverse(List)
     */
    public static <T> List<T> reverse(final List<T> list) {
        return Lists.reverse(list);
    }

    /**
     * List分页函数
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        return Lists.partition(list, size);
    }

    /**
     * 清理掉List中的Null对象
     */
    public static <T> void notNullList(List<T> list) {
        if (isEmpty(list)) {
            return;
        }
        Iterator<T> ite = list.iterator();
        while (ite.hasNext()) {
            T obj = ite.next();
            // 清理掉null的集合
            if (null == obj) ite.remove();
        }
    }

    public static <T> void uniqueNotNullList(List<T> list) {
        if (isEmpty(list)) return;
        Iterator<T> ite = list.iterator();
        Set<T> set = new HashSet<>((int) (list.size() / 0.75F + 1.0F));
        while (ite.hasNext()) {
            T obj = ite.next();
            // 清理掉null的集合
            if (null == obj) {
                ite.remove();
                continue;
            }
            // 清理掉重复的集合
            if (set.contains(obj)) {
                ite.remove();
                continue;
            }
            set.add(obj);
        }
    }

    /**
     * 返回List1与List2并集，产生新List，对比Collection4 ListUtils优化了初始大小
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> result = new ArrayList<E>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * 返回List1与List2交集，产生新List。
     * 对比Collection4 ListUtils改为性能较低但不去重的版本；
     * 对比List.retainAll()，考虑了List中相同元素出现的次数，如"A"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"A"
     */
    public static <T> List<T> intersection(final List<? extends T> list1, final List<? extends T> list2) {
        List<? extends T> smaller = list1;
        List<? extends T> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }
        // 克隆一个可修改的副本
        List<T> newSmaller = new ArrayList<T>(smaller);
        List<T> result = new ArrayList<T>(smaller.size());
        for (final T e : larger) {
            if (newSmaller.contains(e)) {
                result.add(e);
                newSmaller.remove(e);
            }
        }
        return result;
    }

    /**
     * 返回List1与List2差集，产生新List。
     * 对比List.removeAll()，会计算元素出现的次数，如"A"在List1出现两次，而在List2中只出现一次，则差集里会保留一个"A"
     */
    public static <T> List<T> difference(final List<? extends T> list1, final List<? extends T> list2) {
        final List<T> result = new ArrayList<T>(list1);
        final Iterator<? extends T> iterator = list2.iterator();
        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }
        return result;
    }

    /**
     * 返回List1与List2补集，产生新List。
     * 对比Collection4 ListUtils，其并集－交集时，初始大小没有对交集*2
     */
    public static <T> List<T> disjoint(final List<? extends T> list1, final List<? extends T> list2) {
        List<T> intersection = intersection(list1, list2);
        List<T> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }
}