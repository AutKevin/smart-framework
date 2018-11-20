package com.smart4j.framework.aop;

import java.lang.annotation.*;

/**
 * 切面注解
 * 用来定义Controller这类注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /*注解 - 值为Controller或者Service等注解*/
    Class<? extends Annotation> value();
}
