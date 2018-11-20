package com.smart4j.framework.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理链
 */
public class ProxyChain {
    private final Class<?> targetClass;    //代理类
    private final Object targetObject;    //目标对象
    private final Method targetMethod;    //目标方法
    private final MethodProxy methodProxy;    //方法代理
    private final Object[] methodParams;    //方法参数

    private List<Proxy> proxyList = new ArrayList<Proxy>();   //代理列表
    private int proxyIndex = 0;    //代理索引

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    /**
     * 调用代理类及目标类
     * 这个比较有意思,通过代理链的实例proxyChain不断调用此方法,每次调用都会拿出list中的一个代理执行doProxy方法
     * @return 返回目标类执行的结果
     * @throws Throwable
     */
    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex<proxyList.size()){   //如果代理索引小于代理列表大小
            //从列表中取出Proxy对象，调用器doProxy方法
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        }else {    //所有代理遍历完后
            methodResult = methodProxy.invokeSuper(targetObject,methodParams);  //执行目标对象业务
        }
        return methodResult;
    }
}
