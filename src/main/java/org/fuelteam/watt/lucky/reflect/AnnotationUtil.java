package org.fuelteam.watt.lucky.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import com.google.common.collect.Sets;

public class AnnotationUtil {

    public static Set<Annotation> getAllAnnotations(final Class<?> clazz) {
        List<Class<?>> allTypes = ClassUtil.getAllSuperclasses(clazz);
        allTypes.addAll(ClassUtil.getAllInterfaces(clazz));
        allTypes.add(clazz);

        Set<Annotation> annotations = Sets.newHashSet();
        for (Class<?> type : allTypes) {
            annotations.addAll(Arrays.asList(type.getDeclaredAnnotations()));
        }
        Set<Annotation> superAnnotations = new HashSet<Annotation>();
        for (Annotation ann : annotations) {
            getSuperAnnotations(ann.annotationType(), superAnnotations);
        }
        annotations.addAll(superAnnotations);
        return annotations;
    }

    private static <A extends Annotation> void getSuperAnnotations(Class<A> annotationType, Set<Annotation> visited) {
        Annotation[] annotations = annotationType.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (!annotation.annotationType().getName().startsWith("java.lang") && visited.add(annotation)) {
                getSuperAnnotations(annotation.annotationType(), visited);
            }
        }
    }

    public static <T extends Annotation> Set<Field> getAnnotatedPublicFields(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) return Collections.emptySet();
        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) annotatedFields.add(field);
        }
        return annotatedFields;
    }

    public static <T extends Annotation> Set<Field> getAnnotatedFields(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) return Collections.emptySet();
        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) annotatedFields.add(field);
        }
        annotatedFields.addAll(getAnnotatedFields(clazz.getSuperclass(), annotation));
        return annotatedFields;
    }

    public static <T extends Annotation> Set<Method> getAnnotatedPublicMethods(Class<?> clazz, Class<T> annotation) {
        // 已递归到Object停止
        if (Object.class.equals(clazz)) return Collections.emptySet();

        List<Class<?>> ifcs = ClassUtils.getAllInterfaces(clazz);
        Set<Method> annotatedMethods = new HashSet<Method>();
        // 遍历当前类的所有公共方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // 如果当前方法有标注或定义了该方法的所有接口有标注
            if (method.getAnnotation(annotation) != null || searchOnInterfaces(method, annotation, ifcs)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private static <T extends Annotation> boolean searchOnInterfaces(Method method, Class<T> clazz, List<Class<?>> ifcs) {
        for (Class<?> iface : ifcs) {
            try {
                Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                if (equivalentMethod.getAnnotation(clazz) != null) return true;
            } catch (NoSuchMethodException ex) { // NOSONAR
                // Skip this interface as it doesn't have the method
            }
        }
        return false;
    }
}