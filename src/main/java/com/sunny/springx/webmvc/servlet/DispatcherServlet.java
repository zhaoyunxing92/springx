/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.webmvc.servlet;


import com.sunny.springx.webmvc.annotation.Autowried;
import com.sunny.springx.webmvc.annotation.Controller;
import com.sunny.springx.webmvc.annotation.RequestMapping;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //handMapping
    private List<Handler> handerMapping = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("500 " + e.getMessage());
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {

        Handler handler = getHeadler(req);
        //url 不存在４０４
        if (Objects.isNull(handler)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("not found url > " + req.getRequestURI());
            return;
        }

        // 获取请求参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        // 获取方法
        Object invoke = handler.method.invoke(handler.controller, "");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(invoke.toString());
    }

    private Handler getHeadler(HttpServletRequest req) {
        if (handerMapping.isEmpty()) return null;
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handerMapping) {
            Matcher matcher = handler.pattern.matcher(url);
            if (!matcher.matches()) continue;
            return handler;
        }
        return null;
    }


    @Override
    public void init(ServletConfig config) {
        Instant start = Instant.now();
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

        Instant end = Instant.now();

        System.out.println("springx init in " + Duration.between(start, end).toMillis() + " ms");
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 获取类
            Class<?> clazz = entry.getValue().getClass();
            //没有controller注解的跳过
            if (!clazz.isAnnotationPresent(Controller.class)) continue;
            //根ｕｒｌ
            String rootUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                rootUrl = requestMapping.value();
            }

            //扫描类全部方法
            for (Method method : clazz.getMethods()) {

                //只处理RequestMapping注解
                if (!method.isAnnotationPresent(RequestMapping.class)) continue;
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                // 拼接rootUrl 全局替换避免两个斜杠 正则
                String reg = ("/" + rootUrl + requestMapping.value()).replaceAll("/+", "/");

                handerMapping.add(new Handler(method, entry.getValue(), Pattern.compile(reg)));
                System.out.println("mapping:" + reg + "," + method);
            }
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
                    beanId = field.getName();
                }
                //设置授权
                field.setAccessible(true);
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
                //beanId 默认类小写
                String beanId;
                if (clazz.isAnnotationPresent(Controller.class)) {// 处理Controller注解
                    Controller controller = clazz.getAnnotation(Controller.class);
                    beanId = controller.value();

                    if (StringUtils.isBlank(beanId)) {
                        beanId = StringUtils.lowerFirstCase(clazz.getSimpleName());
                    }

                    ioc.put(beanId, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) { // 处理Service 注解
                    // 获取注解
                    Service service = clazz.getAnnotation(Service.class);

                    beanId = service.value();
                    if (StringUtils.isBlank(beanId)) {
                        beanId = StringUtils.lowerFirstCase(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanId, instance);
                    // 获取类的实现
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        ioc.put(StringUtils.lowerFirstCase(anInterface.getSimpleName()), instance);
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载路径下的全部class name
     *
     * @param scanPackage 包开始扫描路径
     */
    private void doScanner(String scanPackage) {
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

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    private class Handler {
        protected Method method;
        //方法对象实力
        protected Object controller;
        //url正则
        protected Pattern pattern;

        protected Handler(Method method, Object controller, Pattern pattern) {
            this.method = method;
            this.controller = controller;
            this.pattern = pattern;
        }
    }
}
