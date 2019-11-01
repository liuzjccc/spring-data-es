package com.example.springdataes.utils;

import com.google.common.base.CaseFormat;
import org.springframework.util.StringUtils;

/**
 * @author liuzj
 */
public class StringUtil {
    
    /**
     * 字串驼峰转换（exampleStr to example_str）
     *
     * @param str 字串
     * @return String
     */
    public static String stringHumpConversion(String str) {
        if (!StringUtils.isEmpty(str)) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str.trim());
        }
        return str;
    }
}
