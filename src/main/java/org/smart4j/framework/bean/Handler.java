package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * @program: Handler
 * @description: 封装Action信息
 * @author: qiuyu
 * @create: 2018-09-27 15:14
 **/
public class Handler {
    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * Action方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
