package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: ClassHelper
 * @description: 类操作助手
 * 获取所有Controller和Service类的集合
 * @author: qiuyu
 * @create: 2018-09-17 21:58
 **/
public class ClassHelper {

    /**
     * 定义类集合
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取所有Controller类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取所有Service类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下的所有bean类(Controller和Service)
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getServiceClassSet());
        return beanClassSet;
    }

    /**
     * 获取应用包名下某父类(接口)的所有子类(或实现类)
     * @param superClass 父类/接口/类本身(基本数据类型)
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls:CLASS_SET){
            /*！！！！！！！！！！重点！！！！！！！！！！获取某个基类的子类（这样可以获取AOP的所有切面）*/
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)){  //superClass是cls的父类或接口(superClass为cls的父类/接口/本身 && superClass不等于cls)
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下带有指定注解的所有类
     * @param annotationClass 注解
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(annotationClass)){  //如果此类带有annotationClass注解
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
