package org.fuelteam.watt.lucky.context;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        clean();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        clean();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        clean();
        Map<String, T> maps = applicationContext.getBeansOfType(clazz);
        if (maps != null && !maps.isEmpty()) return (T) maps.values().iterator().next();
        return null;
    }

    private static void clean() {
        if (applicationContext == null) throw new NullPointerException("applicationContext is null");
    }
}