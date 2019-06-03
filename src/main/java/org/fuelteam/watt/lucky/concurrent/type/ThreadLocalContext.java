package org.fuelteam.watt.lucky.concurrent.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储于ThreadLocal的Map，用于存储上下文，在高性能场景下可改为EnumMap：
 * 
 * 先定义枚举类，列举所有可能的Key；替换contextMap的创建语句；修改put()/get()中key的类型。举例：
 * <pre>
 * private static ThreadLocal<Map<MyEnum, Object>> contextMap = new ThreadLocal<Map<MyEnum, Object>>() {
 *  &#64;Override
 *  protected Map<MyEnum, Object> initialValue() {
 *   return new EnumMap<MyEnum, Object>(MyEnum.class);
 *  }
 * };
 */
public class ThreadLocalContext {

    private static ThreadLocal<Map<String, Object>> contextMap = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>(16, 0.5f);
        }
    };

    public static void put(String key, Object value) {
        contextMap.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) (contextMap.get().get(key));
    }

    public static void reset() {
        contextMap.get().clear();
    }
}