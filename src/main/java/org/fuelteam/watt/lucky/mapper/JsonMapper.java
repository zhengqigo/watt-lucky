package org.fuelteam.watt.lucky.mapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.fuelteam.watt.lucky.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;

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

    public static JsonMapper nonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    public static JsonMapper defaultMapper() {
        return new JsonMapper();
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException ioe) {
            logger.warn(String.format("write to json string error: %s", object), ioe);
            return null;
        }
    }

    public <T> T fromJson(@Nullable String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException ioe) {
            logger.warn(String.format("parse json string error: %s", jsonString), ioe);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(@Nullable String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) return null;
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException ioe) {
            logger.warn(String.format("parse json string error: %s", jsonString), ioe);
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

    public void update(String jsonString, Object object) {
        try {
            mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
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