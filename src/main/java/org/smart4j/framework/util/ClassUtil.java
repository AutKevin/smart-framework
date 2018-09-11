package org.smart4j.framework.util;/**
 * Created by Administrator on 2018/9/11.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @program: ClassUtil
 * @description: ��d��
 * @author: qiuyu
 * @create: 2018-09-11 19:00
 **/
public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * �@ȡ��d��
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * ���d�
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            //className���ȫ����isInitialized���Ƿ��ʼ���o�B���a�K���o�B�ֶ�
            cls = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
            // e.printStackTrace();
        }
        return cls;
    }

    /**
     * �@ȡָ�������µ������
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){

    }
}
