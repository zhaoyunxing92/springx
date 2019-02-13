/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.webmvc.servlet;

import com.sunny.springx.webmvc.annotation.Autowried;
import com.sunny.springx.webmvc.annotation.Controller;
import com.sunny.springx.webmvc.annotation.Service;
import com.sunny.springx.webmvc.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 02:18
 * @des: 处理全部请求
 */
public class DispatcherServlet extends HttpServlet {

    // 加载配置文件
    //private Properties contextConfig = new Properties();
    //存放class name
    private List<String> classNames = new ArrayList<>();
    // ioc 容器
    private Map<String, Object> ioc = new HashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        //1. 加载配置文件
        //doLoadConfiguration(config.getInitParameter("contextConfigLocation"));
        // 2.扫描相关类
        doScanner(config.getInitParameter("scanPackage"));
        // 3.初始化类
        doInstance();
        //4. 注入
        doAutoWried();
        //5. 初始化url
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 获取类中全部的字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

        }

    }

    private void doAutoWried() {
        if (ioc.isEmpty()) return;

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 获取类中全部的字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {

                // 不包含autowried注解的跳过
                if (!field.isAnnotationPresent(Autowried.class)) continue;

                Autowried autowried = field.getAnnotation(Autowried.class);

                String beanId = autowried.value().trim();
                if (StringUtils.isBlank(beanId)) {
                    beanId = StringUtils.lowerFirstCase(field.getName());
                }

                //设置授权
                field.setAccessible(true);
                System.out.println("getbean>>" + ioc.get(beanId));
                try {
                    // 字段赋值
                    field.set(entry.getValue(), ioc.get(beanId));
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) return;
        try {
            for (String className : classNames) {
                //根据名称实例化加了controller和service注解的类
                Class<?> clazz = Class.forName(className);
                // 处理Controller注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    //key 默认类小写
                    String key = StringUtils.lowerFirstCase(clazz.getSimpleName());
                    ioc.put(key, clazz.newInstance());
                    // 处理Service 注解
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    // 获取注解
                    Service service = clazz.getAnnotation(Service.class);

                    String beanId = service.value();
                    if (StringUtils.isBlank(beanId)) {
                        beanId = StringUtils.lowerFirstCase(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanId, instance);
                    // 一个接口被实现多次后需要异常
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        ioc.put(StringUtils.lowerFirstCase(anInterface.getSimpleName()), instance);
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        ioc.forEach((key, value) -> System.out.println("ioc beans id " + key));
    }

    /**
     * 加载路径下的全部class name
     *
     * @param scanPackage 包开始扫描路径
     */
    private void doScanner(String scanPackage) {
        System.out.println("==========开始扫描[" + scanPackage + "]包下面的文件==========");
        // com.sunny.springx.example 路径替换文件路径
        URL url = getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        assert url != null;
        File classDir = new File(url.getFile());

        for (File file : Objects.requireNonNull(classDir.listFiles())) {
            //如果是文件夹继续扫描
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }

        }
    }

    //    /**
    //     * 加载配置文件
    //     *
    //     * @param contextConfigLocation 配置文件名称
    //     */
   /* private void doLoadConfiguration(String contextConfigLocation) {
        // 通过Properties 读取配置文件
        InputStream resource = getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(resource);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != resource) {
                try {
                    resource.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }*/

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}
