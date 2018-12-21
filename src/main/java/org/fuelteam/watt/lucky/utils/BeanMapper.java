package org.fuelteam.watt.lucky.utils;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

// 简单封装Dozer，实现深度转换Bean<->Bean的Mapper
public class BeanMapper {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    // 将source对象中的属性映射到Class为destinationClass的对象中
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
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