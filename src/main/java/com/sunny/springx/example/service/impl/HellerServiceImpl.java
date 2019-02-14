/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.example.service.impl;

import com.sunny.springx.webmvc.annotation.Service;
import com.sunny.springx.example.service.HellerService;

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
     * @return string
     */
    @Override
    public String say() {
        return " say: hello springx";
    }
}
