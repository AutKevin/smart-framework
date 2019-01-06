package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Param
 * @description: 请求参数对象
 * @author: Created by Autumn
 * @create: 2018-10-24 10:47
 */

public class Param {
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取请求参数映射
     * @return
     */
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();  //所有字段/值的映射map

        if (CollectionUtil.isNotEmpty(formParamList)){
            for (FormParam formParam:formParamList){
                String fieldName = formParam.getFieldName();   //表单参数名
                Object fieldValue = formParam.getFieldValue();   //表单参数值
                /*遍历表单list不能直接放入Map中,因为可能有重复的*/
                if (fieldMap.containsKey(fieldName)){   //如果已经有此参数名
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;  // 旧的数据<-->新的数据作为value
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String,List<FileParam>>();
        if (CollectionUtil.isNotEmpty(fileParamList)){  //判断全局变量、不是局部变量
            for (FileParam fileParam:fileParamList){    //遍历文件参数
                String fieldName = fileParam.getFieldName();    //获取表单文件字段名
                List<FileParam> fileParamList;  //上传的多个文件
                if (fileMap.containsKey(fieldName)){    //如果Map已经存在,即放入第二个文件
                    fileParamList = fileMap.get(fieldName);   //获取表单文件名对应的List
                }else{
                    fileParamList = new ArrayList<FileParam>();  //否则,新建一个值
                }
                fileParamList.add(fileParam);   //上传的多个文件List
                fileMap.put(fieldName,fileParamList);   //放入到Map<表单文件字段名,List<FileParam>>的映射中
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件(多文件上传)
     * @param fieldName 表单文件字段名
     * @return
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     * @param fieldName 表单文件字段名
     * @return
     */
    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() ==1){
            return fileParamList.get(0);
        }
        return null;
    }

    /**
     * 验证参数是否为空
     * @return
     */
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }

    /**
     * 根据参数名获取String型参数值
     * @param name
     * @return
     */
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Double型参数值
     * @param name
     * @return
     */
    public Double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Long型参数值
     * @param name
     * @return
     */
    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }
    /**
     * 根据参数名获取int型参数值
     * @param name
     * @return
     */
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }
    /**
     * 根据参数名获取boolean型参数值
     * @param name
     * @return
     */
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFieldMap().get(name));
    }


    /*//未重构参数封装类 - 用一个简单的Map做为键值对映射
    private Map<String,Object> paramMap;
    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    *//**
     * 根据参数名获取long型参数值
     * @param name
     * @return
     *//*
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    *//**
     * 获取所有字段信息
     * @return
     *//*
    public Map<String,Object> getMap(){
        return paramMap;
    }

    *//**
     * 验证参数是否为空
     * @return
     *//*
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(paramMap);
    }*/
}

