package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e){
            return "";
        }
    }
}
