package com.example.remote.utils;

/**
 * 通用工具类
 */
public class CommonUtils {

    /**
     * 判断字符串是否为null或者“”
     * @param values 多个字符串
     * @return 如果有一个字符串为null或者为“” ,返回true；如果都不为空或者""则返回false
     */
    public static boolean isNullOrBlank(String... values){
        for (String value:values) {
            if (value == null || value.equals("")){
                return true;
            }
        }
        return false;
    }

}
