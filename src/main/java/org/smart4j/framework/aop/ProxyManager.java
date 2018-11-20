package org.smart4j.framework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 */
public class ProxyManager {
    /**
     * 根据目标类和代理列表创建一个代理链
     * @param targetClass  目标类
     * @param proxyList  代理列表
     * @param <T> 返回一个代理链执行的返回结果
     * @return
     */
    public static <T> T createProxy(final Class<T> targetClass, final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                //每个方法执行都会执行性代理链的doProxyChain方法(这里每执行一个目标类的方法,都会new一个代理链并执行该代理链)
                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }
}
