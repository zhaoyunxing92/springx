/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.webmvc;

import com.sunny.springx.webmvc.servlet.DispatcherServlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * @author sunny
 * @date: 2019-02-13 00:54
 * @des: 容器初始化利用spi机制注入DispatcherServlet
 */
public class SpringxServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) {
        ServletRegistration.Dynamic dispatcherServlet = ctx.addServlet("dispatcherServlet", DispatcherServlet.class);
        dispatcherServlet.setInitParameter("scanPackage", "com.sunny.springx.example");
        dispatcherServlet.addMapping("/*");
        dispatcherServlet.setLoadOnStartup(1);
    }
}
