package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: ServletHelper
 * @description: Servlet助手类
 * @author: Created by qy
 * @create: 2019-01-27 19:00
 */
public class ServletHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletHelper.class);

    /**
     * 使每个线程独自拥有一份ServletHelper实例
     */
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHelper>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 初始化
     * @param request
     * @param response
     */
    public static void init(HttpServletRequest request,HttpServletResponse response){
        SERVLET_HELPER_HOLDER.set(new ServletHelper(request,response));
    }

    /**
     * 销毁
     */
    public static void destroy(){
        SERVLET_HELPER_HOLDER.remove();
    }

    /**
     * 获取Request对象
     * @return
     */
    private static HttpServletRequest getRequest(){
        return SERVLET_HELPER_HOLDER.get().request;
    }

    /**
     * 获取Response对象
     * @return
     */
    private static HttpServletResponse getResponse(){
        return SERVLET_HELPER_HOLDER.get().response;
    }

    /**
     * 获取Session对象
     * @return
     */
    private static HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 获取ServletContext对象
     * @return
     */
    private static ServletContext getContext(){
        return getRequest().getServletContext();
    }

    /**
     * 将属性放入Request中
     * @param key
     * @param val
     */
    public static void setRequestAttribute(String key,Object val){
        getRequest().setAttribute(key,val);
    }

    /**
     * 获取Request中的属性
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getRequestAttribute(String key){
        return (T) getRequest().getAttribute(key);
    }

    /**
     * 从Request中移除属性
     * @param key
     */
    public static void removeRequestAttribute(String key){
        getRequest().removeAttribute(key);
    }

    /**
     * 重定向
     * @param location
     */
    public static void sendRedirect(String location){
        try {
            getResponse().sendRedirect(location);
        } catch (IOException e) {
            LOGGER.error("redirect failure",e);
        }
    }

    /**
     * 将属性放入Session中
     * @param key
     * @param val
     */
    public static void setSessionAttribute(String key,Object val){
        getSession().setAttribute(key,val);
    }

    /**
     * 获取Session中的属性
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getSessionAttribute(String key){
        return (T) getSession().getAttribute(key);
    }

    /**
     * 移除Session中的属性
     * @param key
     */
    public static void removeSessionAttribute(String key){
        getSession().removeAttribute(key);
    }

    /**
     * 使Session失效
     */
    public static void invalidateSession(){
        getSession().invalidate();
    }
}