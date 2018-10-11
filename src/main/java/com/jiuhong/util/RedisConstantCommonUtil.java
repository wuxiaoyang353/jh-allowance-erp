package com.jiuhong.util;

/**
 * Redis常量定义工具类
 * Created by za-wuxiaoyang on 2018/9/11.
 */
public class RedisConstantCommonUtil {

    /**
     * 用户token常量
     */
    public static final String JH_USRE_TOKEN = "JH:USER_TOKEN:%s";

    /**
     * 用户userId常量
     */
    public static final String JH_USRE_ID = "JH:USER_ID:%s";

    /**
     * 获取对应 key
     *
     * @param constantKry 常量 key
     * @param param1
     * @return
     */
    public static String getKey(String constantKry, String param1) {
        return String.format(constantKry, param1);
    }
}
