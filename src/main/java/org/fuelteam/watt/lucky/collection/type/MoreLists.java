package org.fuelteam.watt.lucky.collection.type;

import java.util.Comparator;

public class MoreLists {

    /**
     * 排序的ArrayList，插入时排序，不支持指定插入index的方法，如add(index, element)
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Comparable> SortedArrayList<T> createSortedArrayList() {
        return new SortedArrayList<T>();
    }

    /**
     * 排序的ArrayList，插入时排序，不支持指定插入index的方法，如add(index, element)
     */
    public static <T> SortedArrayList<T> createSortedArrayList(Comparator<? super T> c) {
        return new SortedArrayList<T>(c);
    }
}