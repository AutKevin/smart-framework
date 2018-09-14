package org.smart4j.framework.util;/**
 * Created by Administrator on 2018/9/11.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.StringUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
     * 将类加载到方法区中
     * @param className 类名 packageName.className
     * @param isInitialized 是否初始化静态代码块和静态字段
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
     * 獲取指定包名下的所有類和jar包
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if (url!=null){
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")){ //如果是文件
                        String packagePath = url.getPath().replaceAll("20%"," ");
                        addClass(classSet,packagePath,packageName);
                    }else if (protocol.equals("jar")){  //如果是jar包
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();  //根据url获取jar的Connection
                        url.openConnection();
                        if (jarURLConnection!=null){
                            JarFile jarFile = jarURLConnection.getJarFile();   //根据connection获取jar文件
                            if (jarFile!=null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();  //获取jar文件中的实体枚举
                                while (jarEntries.hasMoreElements()){  //遍历枚举
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")){
                                        String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 加载指定包下面的jar和class文件
     * @param classSet 存class的集合
     * @param packagePath 包的真实路径  D:***
     * @param packageName 包名  *.*.*
     */
    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        //System.out.println(packageName);
        //System.out.println(packagePath);
        //获得包下面的所有文件(.class和文件夹)
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            //文件过滤器，只获取.class结尾的文件和文件将夹
            public boolean accept(File file) {
                return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for (File file:files){
            String fileName = file.getName();   //获取文件名
            if (file.isFile()){  //如果是文件
                String className = fileName.substring(0,fileName.lastIndexOf('.'));
                if (StringUtil.isNotEmpty(packageName)){
                    className = packageName+"."+className;  //根据传进来的包名packageName和获取的.class文件名拼接成packageName.className
                    doAddClass(classSet,className);
                }
            }else {  //如果是目录
                String subPackagePath = fileName;
                if (StringUtil.isNotEmpty(packagePath)){
                    subPackagePath=packagePath+"/"+subPackagePath;  //子目录路径
                }

                String subPackageName = fileName;
                if (StringUtil.isNotEmpty(subPackageName)){
                    subPackageName=packageName+"."+subPackageName;  //子包名
                }
                addClass(classSet,subPackagePath,subPackageName);  // 将子包和子包路径传进去
            }
        }
    }

    /**
     * 将类加载到方法区,并放入Set集合中
     * @param classSet 存被加载类的集合
     * @param className 类的全名 packageName.className
     */
    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass(className,false);
        classSet.add(cls);
    }

    public static void main(String[] args) {
        //loadClass("org.smart4j.framework.util.Cls",false);
        //Cls cls = new Cls();
        Set<Class<?>> classSet = getClassSet("org.smart4j.framework.util");
        System.out.println(classSet);
    }
}
