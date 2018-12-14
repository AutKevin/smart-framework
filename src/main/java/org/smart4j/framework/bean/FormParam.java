package org.smart4j.framework.bean;

/**
 * @program: FormParam
 * @description: 封装表单参数
 * @author: Created by Autumn
 * @create: 2018-12-14 14:41
 */
public class FormParam {
    private String fieldName;   //表单字段名
    private Object fieldValue;   //表单字段值

    public FormParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
