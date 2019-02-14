/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.example.controller;

import com.sunny.springx.example.service.HellerService;
import com.sunny.springx.webmvc.annotation.Autowried;
import com.sunny.springx.webmvc.annotation.Controller;
import com.sunny.springx.webmvc.annotation.RequestMapping;
import com.sunny.springx.webmvc.annotation.RequestParam;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 02:51
 * @des:
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowried
    private HellerService hellerService;

    @RequestMapping("/say")
    public String controllerSay(@RequestParam("name") String name) {

        return hellerService.say(name);
    }
}
