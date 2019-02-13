/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.example.impl;

import com.sunny.springx.webmvc.annotation.Service;
import com.sunny.springx.example.HellerService;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 02:53
 * @des:
 */
@Service
public class HellerServiceImpl implements HellerService {
    /**
     * say
     *
     * @param name 姓名
     * @return string
     */
    @Override
    public String say(String name) {
        return name + " say: hello springx";
    }
}
