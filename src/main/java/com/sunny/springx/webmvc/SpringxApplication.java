package com.sunny.springx.webmvc;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * @author zhaoyunxing
 * @date: 2019-02-14 12:58
 * @des:
 */
public class SpringxApplication {

    public static void run() {

        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(7000);
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
