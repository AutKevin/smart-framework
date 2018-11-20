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
                IocHelper.class,
                ControllerHelper.class,
                AopHelper.class
        };

        for (Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
