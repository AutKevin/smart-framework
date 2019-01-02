package org.smart4j.framework.helper;

import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @program: RequestHelper
 * @description: 请求助手类
 * @author: Created by Autumn
 * @create: 2018-12-25 13:22
 */
public class RequestHelper {

    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    /**
     * 获取Form表单普通参数并放入List<FormParam>中
     * 适用于application/x-www-form-urlencoded
     * @param request
     * @return List<FormParam>
     */
    private static List<FormParam> parseParameterNames(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();   //获取request中的所有参数名称枚举
        while (paramNames.hasMoreElements()){   //遍历参数名枚举
            String fieldName = paramNames.nextElement();   //获取参数名称
            //!!!!!!!!获取参数值(例如CheckBox的值有多个) request.getParameter(String name)是获得相应名的数据，如果有重复的名，则返回第一个的值.
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)){   //判断是否为空
                Object fieldValue;   //参数最终值
                if (fieldValues.length == 1){  //如果只有一个值
                    fieldValue = fieldValues[0];   //直接赋值
                } else {  //如果有多个值(CheckBox多选)
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i< fieldValues.length; i++){  //遍历
                        sb.append(fieldValues[i]);
                        if (i != fieldValues.length-1){  //如果不是最后一个
                            sb.append(StringUtil.SEPARATOR);  //加上通用分割符
                        }
                    }
                    fieldValue = sb.toString();

                }
                formParamList.add(new FormParam(fieldName,fieldValue));   //将参数键值对加入List参数列表中去
            }
        }
        return formParamList;
    }

    /**
     * 获取参数流并放入List<FormParam>中
     * 适用于application/json，text/xml，multipart/form-data文本流或者大文件形式提交的请求或者xml等形式的报文
     * @param request
     * @return
     * @throws IOException
     */
    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();

        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)){
            String[] kvs = StringUtil.splitString(body,"&");
            if (ArrayUtil.isNotEmpty(kvs)){
                for (String kv:kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if (ArrayUtil.isNotEmpty(array) && array.length == 2){
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParamList.add(new FormParam(fieldName,fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
