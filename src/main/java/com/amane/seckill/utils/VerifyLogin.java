package com.amane.seckill.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Pattern;

public class VerifyLogin {
    private static final Pattern mobile_pattern = Pattern.compile("^1[3456789]\\d{9}$");

    public static boolean isMobile(String phone){
        if(StringUtils.isEmpty(phone)){
            return false;
        }
        return mobile_pattern.matcher(phone).matches();
    }
}
