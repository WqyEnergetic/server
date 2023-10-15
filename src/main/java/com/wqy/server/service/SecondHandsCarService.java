package com.wqy.server.service;

import com.wqy.server.pojo.Car;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/25 9:48
 */
public interface SecondHandsCarService {

    /**
     * 初始化二手车列表
     *
     * @return
     */
    List<Car> init();



    /**
     * 过滤后的二手车列表
     *
     * @param parseData
     * @return
     */
    List<Car> filterate(Map<String, Object> parseData);
}
