package com.wqy.server.service;

import com.wqy.server.pojo.Car;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/24 13:23
 */
public interface CarpoolingCarService {

    /**
     * 初始化顺风车列表
     *
     * @return
     */
    List<Car> initCarpoolingList();

    /**
     * 得到过滤后的顺风车列表
     *
     * @param data
     * @return
     */
    List<Car> getFilteredData(Map<String, Object> data);

}
