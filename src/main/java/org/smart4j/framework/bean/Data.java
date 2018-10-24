package org.smart4j.framework.bean;

/**
 * @program: Data
 * @description: 返回数据对象
 * @author: Created by Autumn
 * @create: 2018-10-24 11:12
 */

public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
