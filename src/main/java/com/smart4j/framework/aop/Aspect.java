package com.smart4j.framework.aop;

import java.lang.annotation.*;

/**
 * 切面注解
 * 用来指定切点为哪些注解
 * Controller这类注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /*值为注解,也就是说切点为指定的注解 -  值为Controller或者Service等注解*/
    Class<? extends Annotation> value();
}
