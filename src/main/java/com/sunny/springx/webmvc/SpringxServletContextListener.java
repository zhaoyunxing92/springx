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
 * @des: 监听容器初始化
 */
//@WebListener
public class SpringxServletContextListener  implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ServletRegistration.Dynamic dispatcherServlet = ctx.addServlet("dispatcherServlet", DispatcherServlet.class);
    }

    //    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        //动态注册　dispatcherServlet
//        ServletContext servletContext = sce.getServletContext();
//        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcherServlet", DispatcherServlet.class);
//        //指定扫描的包
//        dispatcherServlet.setInitParameter("scanPackage", "com.sunny.springx.example");
//        dispatcherServlet.addMapping("/*");
//        dispatcherServlet.setLoadOnStartup(1);
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//
//    }
}
