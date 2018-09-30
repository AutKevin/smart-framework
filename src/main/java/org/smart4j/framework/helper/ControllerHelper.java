package org.smart4j.framework.helper;/**
 * Created by Autumn on 2018/9/27.
 */

import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ControllerHelper
 * @description: 控制器助手类
 * @author: QiuYu
 * @create: 2018-09-27 15:34
 **/
public final class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系
     */
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();


}
