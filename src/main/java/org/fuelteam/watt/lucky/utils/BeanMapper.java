package org.fuelteam.watt.lucky.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Array;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

// 简单封装Dozer，实现深度转换Bean<->Bean的Mapper
public class BeanMapper {

    private static DozerBeanMapper dozer = new DozerBeanMapper();
    
    // 将source对象中的属性映射到Class为destinationClass的对象中
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 简单的复制出新对象ArrayList
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                destinationList.add(dozer.map(source, destinationClass));
            }
        }
        return destinationList;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    /**
     * 简单复制出新对象数组
     */
    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = newArray(destinationClass, sourceArray.length);

        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = dozer.map(sourceArray[i], destinationClass);
                i++;
            }
        }

        return destinationArray;
    }

    // 批量map映射
    @SuppressWarnings("rawtypes")
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    // 将source对象中的属性拷贝到对象destinationObject中
    public static <T> T copy(Object source, T destinationObject) {
        dozer.map(source, destinationObject);
        return destinationObject;
    }

    private BeanMapper() {}
}