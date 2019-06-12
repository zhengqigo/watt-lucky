package org.fuelteam.watt.lucky.properties;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fuelteam.watt.lucky.number.NumberUtil;
import org.fuelteam.watt.lucky.utils.BooleanUtil;

public class SystemPropertiesUtil {

    public static Boolean getBoolean(String name) {
        String stringResult = System.getProperty(name);
        return BooleanUtil.toBooleanObject(stringResult);
    }

    public static Boolean getBoolean(String name, Boolean defaultValue) {
        String stringResult = System.getProperty(name);
        return BooleanUtil.toBooleanObject(stringResult, defaultValue);
    }

    public static String getString(String name) {
        return System.getProperty(name);
    }

    public static String getString(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }

    public static Integer getInteger(String name) {
        return Integer.getInteger(name);
    }

    public static Integer getInteger(String name, Integer defaultValue) {
        return Integer.getInteger(name, defaultValue);
    }

    public static Long getLong(String name) {
        return Long.getLong(name);
    }

    public static Long getLong(String name, Long defaultValue) {
        return Long.getLong(name, defaultValue);
    }

    public static Double getDouble(String propertyName) {
        return NumberUtil.toDoubleObject(System.getProperty(propertyName), null);
    }

    public static Double getDouble(String propertyName, Double defaultValue) {
        Double propertyValue = NumberUtil.toDoubleObject(System.getProperty(propertyName), null);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 合并系统变量(-D)、环境变量和默认值
    public static String getString(String propertyName, String envName, String defaultValue) {
        checkEnvName(envName);
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null) return propertyValue;
        propertyValue = System.getenv(envName);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 合并系统变量(-D)、环境变量和默认值
    public static Integer getInteger(String propertyName, String envName, Integer defaultValue) {
        checkEnvName(envName);
        Integer propertyValue = NumberUtil.toIntObject(System.getProperty(propertyName), null);
        if (propertyValue != null) return propertyValue;
        propertyValue = NumberUtil.toIntObject(System.getenv(envName), null);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 合并系统变量(-D)、环境变量和默认值
    public static Long getLong(String propertyName, String envName, Long defaultValue) {
        checkEnvName(envName);
        Long propertyValue = NumberUtil.toLongObject(System.getProperty(propertyName), null);
        if (propertyValue != null) return propertyValue;
        propertyValue = NumberUtil.toLongObject(System.getenv(envName), null);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 合并系统变量(-D)、环境变量和默认值
    public static Double getDouble(String propertyName, String envName, Double defaultValue) {
        checkEnvName(envName);
        Double propertyValue = NumberUtil.toDoubleObject(System.getProperty(propertyName), null);
        if (propertyValue != null) return propertyValue;
        propertyValue = NumberUtil.toDoubleObject(System.getenv(envName), null);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 合并系统变量(-D)、环境变量和默认值
    public static Boolean getBoolean(String propertyName, String envName, Boolean defaultValue) {
        checkEnvName(envName);
        Boolean propertyValue = BooleanUtil.toBooleanObject(System.getProperty(propertyName), null);
        if (propertyValue != null) return propertyValue;
        propertyValue = BooleanUtil.toBooleanObject(System.getenv(envName), null);
        return propertyValue != null ? propertyValue : defaultValue;
    }

    // 检查环境变量名不能有'.'linux不支持
    private static void checkEnvName(String envName) {
        if (envName == null || envName.indexOf('.') != -1) {
            throw new IllegalArgumentException("envName " + envName + "is null or has dot which is not valid");
        }
    }

    public static synchronized void registerSystemPropertiesListener(PropertiesListener listener) {
        Properties currentProperties = System.getProperties();
        // 将System的properties实现替换为ListenableProperties
        if (!(currentProperties instanceof ListenableProperties)) {
            ListenableProperties newProperties = new ListenableProperties(currentProperties);
            System.setProperties(newProperties);
            currentProperties = newProperties;
        }
        ((ListenableProperties) currentProperties).register(listener);
    }

    /**
     * 扩展ListenableProperties所关心属性变化时通知
     * 
     * @see ListenableProperties
     */
    public static class ListenableProperties extends Properties {
        private static final long serialVersionUID = -8282465702074684324L;

        protected transient List<PropertiesListener> listeners = new CopyOnWriteArrayList<PropertiesListener>();

        public ListenableProperties(Properties properties) {
            super(properties);
        }

        public void register(PropertiesListener listener) {
            listeners.add(listener);
        }

        @Override
        public synchronized Object setProperty(String key, String value) {
            Object result = put(key, value);
            for (PropertiesListener listener : listeners) {
                if (listener.propertyName.equals(key)) listener.onChange(key, value);
            }
            return result;
        }
    }

    public abstract static class PropertiesListener {

        protected String propertyName;

        public PropertiesListener(String propertyName) {
            this.propertyName = propertyName;
        }

        public abstract void onChange(String propertyName, String value);
    }
}