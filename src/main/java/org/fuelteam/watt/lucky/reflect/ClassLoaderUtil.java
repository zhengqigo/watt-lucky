package org.fuelteam.watt.lucky.reflect;

public class ClassLoaderUtil {

    // 按顺序获取默认ClassLoader: Thread.currentThread().getContextClassLoader()->ClassLoaderUtil的加载ClassLoader->SystemClassLoader
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) { // NOSONAR
            // falling back as cannot access thread context ClassLoader
        }
        if (loader == null) {
            // use class loader of this class
            loader = ClassLoaderUtil.class.getClassLoader();
            if (loader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) { // NOSONAR
                    // Cannot access system ClassLoader-> well, maybe the caller can live with null
                }
            }
        }
        return loader;
    }

    // 探测classpath中是否存在类
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable ex) { // NOSONAR
            // Class or one of its dependencies is not present
            return false;
        }
    }
}