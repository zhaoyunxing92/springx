/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.webmvc.util;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 02:40
 * @des:
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static Boolean isBlank(String str) {

        return null == str || str.length() == 0;
    }

    /**
     * 首字母小写，首个字符不是字母或者不是小写的字母不处理
     *
     * @param str 字母字符串 A-65 a-97
     * @return 小写后的字符串  null则返回""
     */
    public static String lowerFirstCase(String str) {
        if (isBlank(str)) {
            return "";
        }

        char[] chars = str.toCharArray();
        int value = (int) chars[0];

        //首个字符不是字母或者不是小写的字母不处理
        if (value <= 65 || value >= 97) {
            return str;
        }

        chars[0] += 32;
        return String.valueOf(chars);
    }
}
