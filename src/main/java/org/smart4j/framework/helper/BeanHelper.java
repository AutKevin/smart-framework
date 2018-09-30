package org.smart4j.framework.helper;

import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类
 *
 */
public class BeanHelper {
    /**
     * 定义bean映射(用于存放Bean类与Bean实例的映射关系)
     */
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static{
        //获取所有Controller和Service
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        //遍历所有的Controlller和Service类
        for (Class<?> beanClass:beanClassSet){
            Object obj = ReflectionUtil.newInstance(beanClass);  //将类实例化
            BEAN_MAP.put(beanClass,obj); //将类和实例放入Map中  Map<Class,Obj>
        }
    }

    /**
     * 获取Bean映射
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 根据Class获取bean实例
     * @param cls bean实例所属的类
     * @param <T> 类的实例对象
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class"+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}
