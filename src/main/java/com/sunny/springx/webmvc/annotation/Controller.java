/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 01:50
 * @des:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    String value() default "";
}
