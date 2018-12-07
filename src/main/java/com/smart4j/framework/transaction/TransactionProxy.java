package com.smart4j.framework.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.aop.Proxy;
import org.smart4j.framework.aop.ProxyChain;
import org.smart4j.framework.util.DBHelper;

import java.lang.reflect.Method;

/**
 * @program: TransactionProxy
 * @description: 事务切面
 * @author: Created by Autumn
 * @create: 2018-12-07 13:02
 */
public class TransactionProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean>  FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {

                DBHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DBHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                DBHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw  e;
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
