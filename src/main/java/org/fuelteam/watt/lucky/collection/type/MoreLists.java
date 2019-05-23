package org.fuelteam.watt.lucky.collection.type;

import java.util.Comparator;

public class MoreLists {

    @SuppressWarnings("rawtypes")
    public static <T extends Comparable> SortedArrayList<T> createSortedArrayList() {
        return new SortedArrayList<T>();
    }

    public static <T> SortedArrayList<T> createSortedArrayList(Comparator<? super T> c) {
        return new SortedArrayList<T>(c);
    }
}