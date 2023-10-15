package com.wqy.server.service;

import com.wqy.server.pojo.City;
import com.wqy.server.pojo.Store;

import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 提供所有营业网点功能的服务接口
 * @version: 1.0
 * @date: 2023/7/22 15:34
 */
public interface OpeningStoreService {

    /**
     * 通过id查找门店的信息
     *
     * @param id
     * @return
     */
    Store getStoreById(String id);

    /**
     * 通过店名查找门店的信息
     *
     * @param name
     * @return
     */
    Store getStoreByName(String name);

    /**
     * 通过城市名获取其下所有门店
     *
     * @param city
     * @return
     */
    List<Store> getAllStores(String city);

    /**
     * 查询全部城市
     *
     * @return
     */
    List<City> searchAllCities();

    /**
     * 查询常见城市
     *
     * @return
     */
    List<City> getCommonCities();
}
