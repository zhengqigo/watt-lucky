package org.fuelteam.watt.lucky.mapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

public class BeanMapper {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) destinationList.add(dozer.map(source, destinationClass));
        }
        return destinationList;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = newArray(destinationClass, sourceArray.length);
        int i = 0;
        for (S source : sourceArray) {
            if (source == null) continue;
            destinationArray[i] = dozer.map(sourceArray[i], destinationClass);
            i++;
        }
        return destinationArray;
    }

    public static <T> T copy(Object source, T destinationObject) {
        dozer.map(source, destinationObject);
        return destinationObject;
    }

    private BeanMapper() {}
}