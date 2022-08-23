package com.wifosell.lazada.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;

public class LazadaConvertUtils {
    public static <T> T fromJsonStringToObject(String jsonString, Class<T> objectClass) {
        return new Gson().fromJson(jsonString, objectClass);
    }

    public static String fromObjectToXmlString(Object object) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return xmlMapper.writeValueAsString(object);
    }
}
