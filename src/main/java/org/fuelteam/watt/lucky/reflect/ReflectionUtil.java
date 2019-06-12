package org.fuelteam.watt.lucky.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.fuelteam.watt.lucky.exception.ExceptionUtil;
import org.fuelteam.watt.lucky.exception.UncheckedException;
import org.fuelteam.watt.lucky.utils.ObjectUtil;

public class ReflectionUtil {

    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    public static Method getSetterMethod(Class<?> clazz, String propertyName, Class<?> parameterType) {
        String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
        return getMethod(clazz, setterMethodName, parameterType);
    }

    public static Method getGetterMethod(Class<?> clazz, String propertyName) {
        String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
        Method method = getMethod(clazz, getterMethodName);
        // retry on another name
        if (method == null) {
            getterMethodName = IS_PREFIX + StringUtils.capitalize(propertyName);
            method = getMethod(clazz, getterMethodName);
        }
        return method;
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, Class<?>... parameterTypes) {
        Method method = MethodUtils.getMatchingMethod(clazz, methodName, parameterTypes);
        if (method != null) makeAccessible(method);
        return method;
    }

    public static Method getAccessibleMethodByName(final Class<?> clazz, final String methodName) {
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义继续向上转型
            }
        }
        return null;
    }

    // 性能较差, 用于单次调用的场景
    public static <T> T invokeGetter(Object obj, String propertyName) {
        Method method = getGetterMethod(obj.getClass(), propertyName);
        String message = "Could not find getter method [%s] on target [%s]";
        if (method == null) throw new IllegalArgumentException(String.format(message, propertyName, obj));
        return invokeMethod(obj, method);
    }

    // 性能较差, 用于单次调用的场景, 按value类型匹配函数
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Method method = getSetterMethod(obj.getClass(), propertyName, value.getClass());
        String message = "Could not find getter method [%s] on target [%s]";
        if (method == null) throw new IllegalArgumentException(String.format(message, propertyName, obj));
        invokeMethod(obj, method, value);
    }

    // 性能较差, 用于单次调用的场景, 不经过getter函数
    public static <T> T getFieldValue(final Object obj, final String fieldName) {
        Field field = getField(obj.getClass(), fieldName);
        String message = "Could not find field [%s] on target [%s]";
        if (field == null) throw new IllegalArgumentException(String.format(message, fieldName, obj));
        return getFieldValue(obj, field);
    }

    // 可反复调用, 不经过getter函数
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(final Object obj, final Field field) {
        try {
            return (T) field.get(obj);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    // 性能较差, 用于单次调用的场景, 不经过setter函数
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getField(obj.getClass(), fieldName);
        String message = "Could not find field [%s] on target [%s]";
        if (field == null) throw new IllegalArgumentException(String.format(message, fieldName, obj));
        setField(obj, field, value);
    }

    // 可反复调用, 不经过setter函数
    public static void setField(final Object obj, Field field, final Object value) {
        try {
            field.set(obj, value);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    // 性能较差, 用于单次调用的场景
    public static <T> T getProperty(Object obj, String propertyName) {
        Method method = getGetterMethod(obj.getClass(), propertyName);
        if (method != null) return invokeMethod(obj, method);
        return getFieldValue(obj, propertyName);
    }

    // 性能较差, 用于单次调用的场景, 按传入value的类型匹配函数
    public static void setProperty(Object obj, String propertyName, final Object value) {
        Method method = getSetterMethod(obj.getClass(), propertyName, value.getClass());
        if (method != null) invokeMethod(obj, method, value);
        setFieldValue(obj, propertyName, value);
    }

    // 性能较差, 用于单次调用的场景, 根据传入参数的实际类型进行匹配
    public static <T> T invokeMethod(Object obj, String methodName, Object... args) {
        Object[] theArgs = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(theArgs);
        return invokeMethod(obj, methodName, theArgs, parameterTypes);
    }

    // 性能较差, 用于单次调用的场景, 根据传入参数的实际类型进行匹配
    public static <T> T invokeMethod(final Object obj, final String methodName, final Object[] args,
            final Class<?>[] parameterTypes) {
        Method method = getMethod(obj.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] with parameter types:"
                    + ObjectUtil.toPrettyString(parameterTypes) + " on class [" + obj.getClass() + ']');
        }
        return invokeMethod(obj, method, args);
    }

    // 性能较差, 用于单次调用的场景, 只匹配函数名, 如果有多个同名函数调用第一个
    public static <T> T invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj.getClass(), methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on class [" + obj.getClass() + ']');
        }
        return invokeMethod(obj, method, args);
    }

    // 调用预先获取的Method, 用于反复调用的场景
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(final Object obj, Method method, Object... args) {
        try {
            return (T) method.invoke(obj, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapAndUnchecked(e);
        }
    }

    public static <T> T invokeConstructor(final Class<T> clazz, Object... args) {
        try {
            return ConstructorUtils.invokeConstructor(clazz, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapAndUnchecked(e);
        }
    }

    // 改变private/protected的成员变量为可访问, 尽量不进行改变以避免JDK的SecurityManager抱怨
    public static void makeAccessible(Method method) {
        if (!method.isAccessible()
                && (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))) {
            method.setAccessible(true);
        }
    }

    // 改变private/protected的成员变量为可访问, 尽量不进行改变以避免JDK的SecurityManager抱怨
    public static void makeAccessible(Field field) {
        if (!field.isAccessible() && (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
            field.setAccessible(true);
        }
    }

    // 将反射时的checked exception转换为unchecked exception
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if ((e instanceof IllegalAccessException) || (e instanceof NoSuchMethodException)) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new UncheckedException(e);
    }
}