package org.fuelteam.watt.lucky.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtil {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    public static String getShortClassName(final Class<?> clazz) {
        return ClassUtils.getShortClassName(clazz);
    }

    public static String getShortClassName(final String className) {
        return ClassUtils.getShortClassName(className);
    }

    public static String getPackageName(final Class<?> cls) {
        return ClassUtils.getPackageName(cls);
    }

    public static String getPackageName(final String className) {
        return ClassUtils.getPackageName(className);
    }

    public static List<Class<?>> getAllSuperclasses(final Class<?> clazz) {
        return ClassUtils.getAllSuperclasses(clazz);
    }

    public static List<Class<?>> getAllInterfaces(final Class<?> clazz) {
        return ClassUtils.getAllInterfaces(clazz);
    }

    /**
     * @see https://github.com/linkedin/linkedin-utils/blob/master/org.linkedin.util-core/src/main/java/org/linkedin/util/reflect/ReflectUtils.java
     */
    public static boolean isSubClassOrInterfaceOf(Class<?> subclass, Class<?> superclass) {
        return superclass.isAssignableFrom(subclass);
    }

    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) return superClass;
        }
        return clazz;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenericType(final Class<?> clazz) {
        return getClassGenericType(clazz, 0);
    }

    @SuppressWarnings("rawtypes")
    public static Class getClassGenericType(final Class<?> clazz, final int index) {
        Type genericType = clazz.getGenericSuperclass();
        if (!(genericType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();
        if ((index >= params.length) || (index < 0)) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }
}