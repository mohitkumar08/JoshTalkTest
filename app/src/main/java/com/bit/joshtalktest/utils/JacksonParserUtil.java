package com.bit.joshtalktest.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonParserUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T parseStringToObject(final String json, final Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public static String objectToString(final Object reqObject) throws IOException {
        return objectMapper.writeValueAsString(reqObject);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
