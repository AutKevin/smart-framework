package org.smart4j.framework;

/**
 * @program: chapter2->StringUtil
 * @description: 字符串工具类
 * @author: qiuyu
 * @create: 2018-08-15 20:20
 **/
public class StringUtil {

    /**
     * 判断是否为空字符串最优代码
     * @param str
     * @return 如果为空，则返回true
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否非空
     * @param str 如果不为空，则返回true
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static String[] splitString(String str,String spliter){
        return str.split(spliter);
    }
}
