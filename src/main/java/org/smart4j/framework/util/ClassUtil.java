package org.smart4j.framework.util;/**
 * Created by Administrator on 2018/9/11.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @program: ClassUtil
 * @description: 類加載器
 * @author: qiuyu
 * @create: 2018-09-11 19:00
 **/
public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 獲取類加載器
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加載類
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            //className為類全名，isInitialized為是否初始化靜態代碼塊和靜態字段
            cls = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
            // e.printStackTrace();
        }
        return cls;
    }

    /**
     * 獲取指定包名下的所有類
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){

    }
}
