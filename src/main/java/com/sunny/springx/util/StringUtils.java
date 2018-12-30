/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.util;

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
}
