/**
 * Copyright(C) 2018 Hangzhou zhaoyunxing92 Technology Co., Ltd. All rights reserved.
 */
package com.sunny.springx.annotation;

import java.lang.annotation.*;

/**
 * @author zhaoyunxing92
 * @date: 2018-12-31 01:53
 * @des:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowriter {
    
    String value() default "";
}
