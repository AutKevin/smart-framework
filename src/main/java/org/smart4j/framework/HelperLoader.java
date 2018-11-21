package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.ClassUtil;

/**
 * @program: HelperLoader
 * @description: 加载相应的Helper类
 * @author: QiuYu
 * @create: 2018-10-23 16:34
 **/
public class HelperLoader {
    public static void init(){
        Class<?>[] classList={
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,    //AOP容器一定要在IOC之前加载，要先把Bean容器中的实例替换成代理，再去注入才可以
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
