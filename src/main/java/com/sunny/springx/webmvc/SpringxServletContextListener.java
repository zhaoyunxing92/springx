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
 * @des: 监听容器初始化 spi机制
 */
public class SpringxServletContextListener implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ServletRegistration.Dynamic dispatcherServlet = ctx.addServlet("dispatcherServlet", DispatcherServlet.class);
        dispatcherServlet.setInitParameter("scanPackage", "com.sunny.springx.example");
        dispatcherServlet.addMapping("/*");
        dispatcherServlet.setLoadOnStartup(1);
    }
}
