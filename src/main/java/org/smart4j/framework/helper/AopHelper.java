package org.smart4j.framework.helper;

import com.smart4j.framework.transaction.TransactionProxy;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.aop.Aspect;
import org.smart4j.framework.aop.AspectProxy;
import org.smart4j.framework.aop.Proxy;
import org.smart4j.framework.aop.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 方法拦截助手类
 */
public class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static{
        try{
            Map<Class<?>,Set<Class<?>>> proxyMap = createProxyMap();   //获取Map<代理类,Set<目标类>>
            Map<Class<?>,List<Proxy>> targetMap = createTargetMap(proxyMap);  //获取Map<目标类,List<代理实例>>
            for (Map.Entry<Class<?>,List<Proxy>> targetEntry:targetMap.entrySet()){   //遍历Map<目标类,List<代理实例>>
                Class<?> targetClass = targetEntry.getKey();   //目标类
                List<Proxy> proxyList = targetEntry.getValue();    //代理类
                Object proxy = ProxyManager.createProxy(targetClass,proxyList);   //根据目标类和代理集合创建一个代理
                BeanHelper.setBean(targetClass,proxy);   //将Bean容器中目标类对应的实体替换成代理
            }
        }catch (Exception e){
                LOGGER.error("aop failure",e);
        }
    }

    /**
     * 根据Aspect注解（切点）获取所有的代理目标类集合(目标类为Controller等注解的类)
     * @param aspect 代理类注解,用来指定目标类的注解 例如：@Aspect(Controller.class)
     * @return 返回Aspect注解中指定value注解的目标类  例如：带Controller注解的所有类
     * 例如Aspect(Controller.class)是指获取所有Controller注解的类
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect){
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();   //获取值(也是注解)
        if (annotation!=null && !annotation.equals(Aspect.class)){   //获取的value注解不为null，且注解不为Aspect
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));   //加入所有value(切点)指定的注解的类
        }
        return targetClassSet;   //返回所有目标类
    }

    /**
     * 创建所有Map<代理类,Set<代理目标类>>的映射关系
     * @return Map<代理类,Set<代理目标类>>
     * @throws Exception
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();   //结果集<代理类,Set<代理目标类>>
        addAspectProxy(proxyMap);   //添加普通切面
        addTransactionProxy(proxyMap);   //添加事务代理
        return proxyMap;
    }

    /**
     * 添加切面代理
     * @param proxyMap
     */
    private static void addAspectProxy(Map<Class<?>,Set<Class<?>>> proxyMap){
        //获取所有的AspectProxy的子类(代理类集合),即切面,
        /*这个是入口，根据基类来查找所有的切面（代理类），获取所有的切面！！！*/
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet){   //遍历代理类(切面),如ControllerAspect
            if (proxyClass.isAnnotationPresent(Aspect.class)){    //验证基类为AspectProxy且要有Aspect注解的才能为切面。如果代理类的的注解为Aspect(也就是说代理类一定要都切点(注解)才能是切面),例如ControllerAspect代理类的注解为@Aspect(Controller.class)
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);   //获取代理类（切面）的注解
                /*根据注解获取所有的目标类*/
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);   //获取所有的代理目标类集合
                proxyMap.put(proxyClass,targetClassSet);   //加入到结果集Map<代理类,Set<代理目标类>>中
            }
        }
    }

    /**
     * 添加事务代理 - 获取Transaction的class - targets映射关系
     * @param proxyMap
     */
    private static void addTransactionProxy(Map<Class<?>,Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);  //获取所有目标类
        proxyMap.put(TransactionProxy.class,serviceClassSet);   //代理类为Transaction注解
    }

    /**
     * 将Map<代理类,Set<目标类>> proxyMap转为Map<目标类,List<代理类>> targetMap
     * @param proxyMap Map<代理类,Set<目标类>>
     * @return Map<目标类,List<代理类实例>>
     * @throws Exception
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>,List<Proxy>>();   //class - list键值对的map
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyEntry:proxyMap.entrySet()){   //遍历cls - set键值对的map
            Class<?> proxyClass = proxyEntry.getKey();   //获取代理cls
            Set<Class<?>> targetClassSet = proxyEntry.getValue();   //获取目标Set
            for (Class<?> targetClass:targetClassSet){    //遍历目标Set
                Proxy proxy = (Proxy) proxyClass.newInstance();   //实例化代理类
                if (targetMap.containsKey(targetClass)){    //如果Map<Class<?>,List<Proxy>>包含该目标类
                    targetMap.get(targetClass).add(proxy);   //直接将代理类添加到对应目标类的Map中
                }else{
                    List<Proxy> proxyList = new ArrayList<Proxy>();   //如果没有
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
