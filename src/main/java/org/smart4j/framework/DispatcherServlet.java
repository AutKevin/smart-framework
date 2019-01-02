package org.smart4j.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: DispatcherServlet
 * @description: 请求转发器
 * @author: Created by Autumn
 * @create: 2018-10-24 11:34
 */

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化相关Helper类
        HelperLoader.init();
        //获取ServletContext对象(用于注册Servlet)
        ServletContext servletContext = servletConfig.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
        //初始化上传文件大小
        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法与请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        if (requestPath.equals("\favicon.ico")){
            return ;
        }
        //获取Action处理器
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null){
            //获取Controller类机器Bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            Param param;
            if (UploadHelper.isMultipart(req)){   //如果是multipart/form-data stream
                param = UploadHelper.createParam(req);   //multipart方式
            }else{   //如果是非multipart方式提交(即application/x-www-form-urlencoded，application/json，text/xml)
                param = RequestHelper.createParam(req);   //非multipart表单方式
            }

            /*
            //创建请求参数对象
            Map<String,Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            //获取请求body中的参数
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params = StringUtil.splitString(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param:params){
                        String[] array = StringUtil.splitString(param,"=");
                        if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            */

            Object result = null;
            //调用Action方法
            Method actionMethod = handler.getActionMethod();
            /*优化没有参数的话不需要写参数*/
            if (param.isEmpty()){  //如果没有参数
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);  //就不传参数
            }else{  //有参数
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);  //传参数
            }

            //处理Action方法返回值
            if (result instanceof View){
                //返回JSP页面
                handleViewResult((View) result, req, resp);
            }else if (result instanceof Data){
                //返回Json数据
                handleDataResult((Data) result, resp);
            }
        }else{
            LOGGER.error("Request-Handler Mapping get null by Request("+requestMethod+","+requestPath+")");
            throw new RuntimeException("Request-Handler Mapping get null by Request("+requestMethod+","+requestPath+")");
        }

    }

    /**
     * 处理Json格式的数据
     * @param result Data对象
     * @param resp
     * @throws IOException
     */
    private void handleDataResult(Data result, HttpServletResponse resp) throws IOException {
        Data data = result;
        Object model = data.getModel();
        if (model!=null){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    /**
     * 处理视图结果
     * @param result View对象(jsp路径+数据)
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void handleViewResult(View result, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        View view = result;
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)){
            if (path.startsWith("/")){   //如果View的Path以/开头则以项目根目录为根路径
                resp.sendRedirect(req.getContextPath()+path);
            } else {    //如果View的Path没有以/开头,则以配置的APPJSP(/WEB-INF/view/)为根目录
                Map<String,Object> model = view.getModel();
                for (Map.Entry<String,Object> entry:model.entrySet()){
                    req.setAttribute(entry.getKey(),entry.getValue());
                }
                req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
            }

        }
    }
}
