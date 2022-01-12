package com.example.demo.util;

public class ValidationUtil {
    public static boolean isStringEmpty(String input){
        return input == null || input.replace(" ", "").length() == 0;
    }

    public static boolean isLongValueEmpty(Long input){
        return input == null || input<= 0;
    }

}
