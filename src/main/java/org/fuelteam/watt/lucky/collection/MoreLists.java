package org.fuelteam.watt.lucky.collection;

import java.util.Comparator;

public class MoreLists {

    public static <T extends Comparable<?>> SortedArrayList<T> createSortedArrayList() {
        return new SortedArrayList<T>();
    }

    public static <T> SortedArrayList<T> createSortedArrayList(Comparator<? super T> c) {
        return new SortedArrayList<T>(c);
    }
}