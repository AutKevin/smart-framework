package com.smart4j.framework.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

/**
 * 切面代理
 */
public abstract class AspectProxy implements Proxy{
    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 执行链式代理
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        //获取目标类、方法、方法参数
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();    //代理开始时执行begin方法
        try {
            if (intercept(cls,method,params)){    //判断是否拦截此方法
                before(cls,method,params);     //目标方法执行前代理方法before执行
                result = proxyChain.doProxyChain();   //执行下一个代理
                after(cls,method,result);    //目标方法执行后代理方法after执行
            }else {
                result = proxyChain.doProxyChain();    //执行下一个代理
            }
        }catch(Exception e){
            logger.error("proxy failure",e);
            error(cls,method,params,e);    //抛出异常时代理方法error执行
            throw e;
        }finally{
            end();    //代理结束时执行方法end
        }
        return result;
    }
    /*方法开始前执行*/
    public void begin(){
    }

    /**
     * 拦截
     * @param cls 目标类
     * @param method 目标方法
     * @param params 目标方法参数
     * @return 返回是否拦截
     */
    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    /**
     * 前置增强
     * @param cls 目标类
     * @param method 目标方法
     * @param params 目标方法参数
     */
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    }
    /**
     * 后置增强
     * @param cls 目标类
     * @param method 目标方法
     * @param result 目标方法返回结果
     */
    public void after(Class<?> cls, Method method, Object result) throws Throwable {
    }

    /**
     * 抛出增强
     * @param cls 目标类
     * @param method 目标方法
     * @param params 目标方法参数
     * @param e 异常
     * @throws Throwable
     */
    public void error(Class<?> cls, Method method, Object[] params, Exception e) throws Throwable {
    }
    /*方法结束后执行*/
    public void end(){
    }
}
