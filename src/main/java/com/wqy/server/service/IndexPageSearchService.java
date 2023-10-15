package com.wqy.server.service;

import com.wqy.server.pojo.Car;
import com.wqy.server.vo.R;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 首页功能接口
 * @version: 1.0
 * @date: 2023/7/23 21:54
 */
public interface IndexPageSearchService {

    /**
     * 查找可租的车
     * @return
     */
    R searchCars(Map<String,Object> data);
}
