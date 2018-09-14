package org.smart4j.framework.util;

/**
 * Created by Autumn on 2018/9/14.
 */
public class Cls {
    String field1 = null;
    static String field2 = staticfieldMethod();
    String field3 = fieldMethod();

    public String fieldMethod(){
        System.out.println("初始化字段");
        return null;
    }

    public static String staticfieldMethod(){
        System.out.println("初始化静态字段");
        return null;
    }
    static {
        System.out.println("这个是static代码块");
    }

    {
        System.out.println("这个是代码块");
    }

}
