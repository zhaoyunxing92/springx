/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx;

import com.sunny.springx.webmvc.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * @author sunny
 * @date: 2019-02-13 00:54
 * @des: 监听容器初始化
 */
@WebListener
public class SpringxServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("***************");
        //动态注册　dispatcherServlet
        ServletContext servletContext = sce.getServletContext();
        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcherServlet", DispatcherServlet.class);

        dispatcherServlet.addMapping("/*");
        dispatcherServlet.setInitParameter("scanPackage", "com.sunny.springx.demo");
        dispatcherServlet.setLoadOnStartup(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
