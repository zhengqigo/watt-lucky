package org.fuelteam.watt.lucky.mapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.fuelteam.watt.lucky.base.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * 简单封装Jackson实现JSON String<->Java Object转换的Mapper
 */
public class JsonMapper {

    private static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    public static final JsonMapper INSTANCE = new JsonMapper();

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(Include include) {
        mapper = new ObjectMapper();
        if (include != null) mapper.setSerializationInclusion(include);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 只输出非Null的属性到Json字符串的Mapper
     */
    public static JsonMapper nonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    /**
     * 只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    /**
     * 默认的全部输出的Mapper, 区别于INSTANCE，可以做进一步的配置
     */
    public static JsonMapper defaultMapper() {
        return new JsonMapper();
    }

    /**
     * Object可以是POJO，也可以是Collection或数组
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>，复杂Collection如List<Bean>用fromJson(String, JavaType)
     * 
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(@Nullable String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>，contructCollectionType()或contructMapType()构造类型
     * 
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(@Nullable String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) return null;
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    @SuppressWarnings("rawtypes")
    public JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * 当JSON里只含有Bean的部分属性時，更新Bean只覆盖部分属性
     */
    public void update(String jsonString, Object object) {
        try {
            mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
    }

    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}