package org.smart4j.framework.aop;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 执行链式代理
     * @param proxyChain 这个链式代理参数中含有所有的代理类，和目标类的参数（类、方法、方法参数）
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
