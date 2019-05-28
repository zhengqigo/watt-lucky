package org.fuelteam.watt.lucky.properties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import org.fuelteam.watt.lucky.io.URLResourceUtil;
import org.fuelteam.watt.lucky.number.NumberUtil;
import org.fuelteam.watt.lucky.text.Charsets;
import org.fuelteam.watt.lucky.utils.BooleanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Boolean getBoolean(Properties p, String name, Boolean defaultValue) {
        return BooleanUtil.toBooleanObject(p.getProperty(name), defaultValue);
    }

    public static Integer getInt(Properties p, String name, Integer defaultValue) {
        return NumberUtil.toIntObject(p.getProperty(name), defaultValue);
    }

    public static Long getLong(Properties p, String name, Long defaultValue) {
        return NumberUtil.toLongObject(p.getProperty(name), defaultValue);
    }

    public static Double getDouble(Properties p, String name, Double defaultValue) {
        return NumberUtil.toDoubleObject(p.getProperty(name), defaultValue);
    }

    public static String getString(Properties p, String name, String defaultValue) {
        return p.getProperty(name, defaultValue);
    }

    // 从文件路径加载properties，默认utf-8编码，支持"file://"和"classpath:"
    public static Properties loadFromFile(String generalPath) {
        Properties p = new Properties();
        try (Reader reader = new InputStreamReader(URLResourceUtil.asStream(generalPath), Charsets.UTF_8)) {
            p.load(reader);
        } catch (IOException e) {
            logger.warn("Load property from " + generalPath + " failed", e);
        }
        return p;
    }

    public static Properties loadFromString(String content) {
        Properties p = new Properties();
        try (Reader reader = new StringReader(content)) {
            p.load(reader);
        } catch (IOException ignored) { // NOSONAR
        }
        return p;
    }
}