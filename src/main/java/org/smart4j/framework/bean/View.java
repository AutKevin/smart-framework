package org.smart4j.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: View
 * @description: 视图对象
 * @author: Created by Autumn
 * @create: 2018-10-24 11:03
 */

public class View {
    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String,Object> model = new HashMap<String, Object>();

    public View(String path) {
        this.path = path;
    }

    public View(String path, Map<String, Object> model) {
        this.path = path;
        this.model = model;
    }

    public View addModel(String key,Object value){
        model.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}