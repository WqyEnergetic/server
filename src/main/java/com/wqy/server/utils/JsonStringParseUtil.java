package com.wqy.server.utils;

import com.alibaba.fastjson2.JSON;
import com.wqy.server.exception.NoSuchKeyToParseException;

/**
 * @author: wqy
 * @description: 解析json类型的简单字符串
 * @version: 1.0
 * @date: 2023/7/22 22:31
 */
public class JsonStringParseUtil {

    /**
     * 解析出String类型的结果
     *
     * @param data json数据
     * @param key  键值
     * @return
     */
    public static String parseToString(String data, String key) {
        String str;
        try {
            str = JSON.parseObject(data).get(key).toString();
        } catch (NullPointerException e) {
            throw new NoSuchKeyToParseException("no such key in your json:" + "\"" + key + "\"");
        }
        return str;
    }

    /**
     * 解析嵌套一次的json数据，得到String类型的结果
     *
     * @param data     json数据
     * @param firstKey 第一次解析所需的键值
     * @param lastKey  第二次解析所需的键值
     * @return
     */
    public static String parseTwice(String data, String firstKey, String lastKey) {
        String res;
        try {
            //转一次
            String str = JSON.parseObject(data).get(firstKey).toString();
            try {
                res = parseToString(str, lastKey);
            } catch (NullPointerException e) {
                throw new NoSuchKeyToParseException("no such key in your json:" + "\"" + lastKey + "\"");
            }
        } catch (NullPointerException e) {
            throw new NoSuchKeyToParseException("no such key in your json:" + "\"" + firstKey + "\"");
        }
        return res;
    }

    /**
     * 解析出int类型的结果
     *
     * @param data
     * @param key
     * @return
     */
    public static Integer parseToInt(String data, String key) {
        Integer res;
        try {
            res = Integer.parseInt(JSON.parseObject(data).get(key).toString());
        } catch (NullPointerException e) {
            throw new NoSuchKeyToParseException("no such key in your json:" + "\"" + key + "\"");
        }
        return res;
    }

}
